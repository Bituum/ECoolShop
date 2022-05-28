package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.Cart;
import bteam.example.ecoolshop.entity.Role;
import bteam.example.ecoolshop.entity.User;
import bteam.example.ecoolshop.entity.UserRegistrationApply;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.repository.ApplyRepository;
import bteam.example.ecoolshop.repository.CartRepository;
import bteam.example.ecoolshop.repository.RoleRepository;
import bteam.example.ecoolshop.repository.UserRepository;
import bteam.example.ecoolshop.util.MailDigitGenerator;
import bteam.example.ecoolshop.util.fileutils.PhotoHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartRepository cartRepository;
    private final EMailServiceImpl eMailService;
    private final ApplyRepository applyRepository;
    private final ObjectMapper objectMapper;
    private final PhotoHandler photoHandler;

    @Value("${apply.expired.time}")
    private int applyLifeTime;

    @Value("${application.content.photoPath}")
    private String PATH;


    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CartRepository cartRepository, EMailServiceImpl eMailService, ApplyRepository applyRepository, ObjectMapper objectMapper, PhotoHandler photoHandler) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.cartRepository = cartRepository;
        this.eMailService = eMailService;
        this.applyRepository = applyRepository;
        this.objectMapper = objectMapper;
        this.photoHandler = photoHandler;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void startRegistrationProcess(String username) {
        applyRepository.save(
                UserRegistrationApply.builder()
                        .createdAt(LocalDateTime.now())
                        .expiredAt(LocalDateTime.now().plus(applyLifeTime, ChronoUnit.HOURS))
                        .username(username)
                        .digitCode(MailDigitGenerator.generate())
                        .build()
        );
    }

    public boolean isDigitKeyExpired(String username) {
        UserRegistrationApply apply = applyRepository.getUserRegistrationAppliesByUsername(username)
                .orElseThrow(
                        UserNotFoundException::new
                );

        LocalDateTime expiredAt = apply.getExpiredAt();

        return LocalDateTime.now().isAfter(expiredAt);
    }

    @Transactional
    public void verificationConfirmation(String username) {
        applyRepository.deleteByUsername(username);
    }

    @Transactional
    public void createUser(User user) {
        Set<Role> userRole = new HashSet<>();
        Cart cart = new Cart();

        cart.setUser(user);
        initRoleAndPassword(user);

        userRepository.save(user);
        cart.setId(user.getUser_id());
        cartRepository.save(cart);
    }

    public User getUserById(int id) {
        return userRepository
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("wrong user id provided")
                );
    }

    public int getIdByUsername(String username) {
        return userRepository
                .findIdByUsername(username)
                .orElseThrow(
                        UserNotFoundException::new
                );
    }

    @Transactional(rollbackOn = RuntimeException.class)
    public void deleteUser(String username) {
        int id = userRepository.findIdByUsername(username).orElseThrow(
                UserNotFoundException::new
        );

        cartRepository.manyToManyCascadeDelete(id);
        cartRepository.deleteById(id);
        userRepository.deleteUserByUsername(username);
    }

    public boolean isUsernameExist(String username) {
        return userRepository
                .findUserByUsername(username)
                .isPresent();
    }

    public void updateUser(UserDto userDto) {
        User user = new User(
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getBirthday(),
                userDto.getPassword()
        );

        initRoleAndPassword(user);

        user.setUser_id(userRepository
                .findIdByUsername(user.getUsername())
                .orElseThrow(
                        UserNotFoundException::new
                )
        );

        userRepository.save(user);
    }

    public void updateUser(User user) {
        //refresh role and password for user entity
        initRoleAndPassword(user);

        userRepository.save(user);
    }

    public void matchThePassword(UserDto userDto) {
        User userByUsername = userRepository
                .findUserByUsername(userDto.getUsername())
                .orElseThrow(
                        UserNotFoundException::new
                );

        if (!bCryptPasswordEncoder.matches(new String(userDto.getPassword()), new String(userByUsername.getPassword()))) {
            throw new IllegalArgumentException("passwords are not the same!");
        }
    }

    private void initRoleAndPassword(User user) {
        Set<Role> userRole = new HashSet<>();

        //noinspection OptionalGetWithoutIsPresent
        userRole.add(roleRepository
                .findById(1)
                .get()
        );
        user.setUserRole(userRole);

        user.setPassword(bCryptPasswordEncoder.encode(
                                new String(user.getPassword())
                        )
                        .toCharArray()
        );
    }

    public User applyPatchToUser(JsonPatch patch, User user) throws JsonProcessingException, JsonPatchException {
        JsonNode patched = patch.apply(objectMapper.convertValue(user, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    /*
     *  @throws  FileCreationException
     *  If a trouble cause during the photo creation
     *  @throws  UserNotFoundException
     *  If there are no user in database
     */
    public void updateUserPhoto(String username, MultipartFile file) {
        String photoPath = photoHandler.handle(file, username, PATH);

        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow(
                        UserNotFoundException::new
                );

        user.setPhotoPath(photoPath);

        userRepository.save(user);
    }
}
