package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.Role;
import bteam.example.ecoolshop.entity.User;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.repository.IRoleRepository;
import bteam.example.ecoolshop.repository.IUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void addNewUser(User user) {
        Set<Role> userRole = new HashSet<>();

        initRoleAndPassword(user);

        userRepository.save(user);
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("user_id is not exist")
        );
    }

    public int getIdByUsername(String username) {
        return userRepository.findIdByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("wrong username!")
        );
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteUserByUsername(username);
    }

    public void updateUser(UserDto userDto) {
        User user = new User(userDto.getUsername(), userDto.getEmail(), userDto.getBirthday(), userDto.getPassword());

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
        userRole.add(roleRepository.findById(1).get());
        user.setUserRole(userRole);

        user.setPassword(bCryptPasswordEncoder.encode(
                        new String(user.getPassword()))
                .toCharArray()
        );
    }
}
