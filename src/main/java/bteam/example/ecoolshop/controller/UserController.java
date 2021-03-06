package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.AUser;
import bteam.example.ecoolshop.exception.FileCreationException;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.repository.ApplyRepository;
import bteam.example.ecoolshop.service.EmailService;
import bteam.example.ecoolshop.service.UserService;
import bteam.example.ecoolshop.util.ConversationUtil;
import bteam.example.ecoolshop.util.JwtTokenUtil;
import bteam.example.ecoolshop.util.MessageWrapper;
import bteam.example.ecoolshop.util.maps.ResponseMap;
import bteam.example.ecoolshop.util.maps.UserMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NonUniqueResultException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bteam.example.ecoolshop.util.ExceptionMapBuilder.getStringStringMap;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;
    private final ConversationUtil<UserDto, AUser> conversationToUserUtil;
    private final ConversationUtil<AUser, UserDto> conversationToDtoUtil;
    private final UserMap userMap;
    private final ApplyRepository applyRepository;
    private final EmailService emailService;

    public UserController(UserService userService, JwtTokenUtil tokenUtil, ConversationUtil<UserDto, AUser> conversationToUserUtil, ConversationUtil<AUser, UserDto> conversationToDtoUtil, UserMap userMap, ApplyRepository applyRepository, EmailService emailService) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
        this.conversationToUserUtil = conversationToUserUtil;
        this.conversationToDtoUtil = conversationToDtoUtil;
        this.userMap = userMap;
        this.applyRepository = applyRepository;
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> mailVerification(@Valid @RequestBody UserDto userDto) {
        userService.startRegistrationProcess(userDto.getUsername());
        try {
            String digitCode = applyRepository.getUserRegistrationAppliesByUsername(userDto.getUsername())
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "empty userRegistrationApply")
                    )
                    .getDigitCode();

            userMap.put(digitCode, userDto);

            emailService.sendSimpleMessage(
                    MessageWrapper.builder()
                            .to(userDto.getEmail())
                            .header("digital code")
                            .text(digitCode)
                            .build()
            );
        } catch (NonUniqueResultException nonUniqueResultException) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "username is already awaits the verification");
        }
        return ResponseEntity.ok("Mail has been send");
    }

    @PostMapping("/signup/{code}")
    public ResponseEntity<String> createUser(@Size(max = 6) @PathVariable("code") String digitCode) {
        UserDto userDto = userMap.get(digitCode)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid digital code provided")
                );

        if (userService.isUsernameExist(userDto.getUsername()) || userService.isDigitKeyExpired(userDto.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "username is already exist or digital key is expired"
            );
        }
        userService.verificationConfirmation(userDto.getUsername());
        conversationToUserUtil.createOrConvert(userDto, AUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body("user was created");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> authorize(@RequestBody UserDto userDto) {
        try {
            userService.matchThePassword(userDto);

            AUser convertedAUser = conversationToUserUtil.createOrConvert(userDto, AUser.class);
            String token = tokenUtil.create(convertedAUser);

            return ResponseEntity.ok().body(ResponseMap.OkResponse("token", token));
        } catch (IllegalArgumentException argumentException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "passwords doesnt match", argumentException
            );
        }
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.getAll()
                .stream()
                .map(user -> conversationToDtoUtil.convertToDto(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Map<Object, Object>> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);

        return ResponseEntity.ok(ResponseMap.OkResponse("Deleted:", username));
    }

    @PutMapping("/")
    public ResponseEntity<Map<Object, Object>> refreshUser(@Valid @RequestBody UserDto userDto) {
        userService.updateUser(userDto);

        return ResponseEntity.ok(ResponseMap.OkResponse("Updated:", userDto.getUsername()));
    }

    /*
     * Example of the request
     * [{"op":"replace",
     * "path":"/username",
     * "value":"patchuser"
     * }]
     *
     * */
    @PatchMapping(path = "/{username}", consumes = "application/json-patch+json")
    public ResponseEntity<String> patchUser(@PathVariable("username") String username, @RequestBody JsonPatch patch) {
        try {
            int userId = userService.getIdByUsername(username);
            AUser AUser = userService.getUserById(userId);
            AUser patchedAUser = userService.applyPatchToUser(patch, AUser);

            userService.updateUser(patchedAUser);

            return ResponseEntity.ok(patchedAUser.getUsername() + "was successful patched");
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserNotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getStringStringMap(ex);
    }

    @PostMapping(path = "/{username}/photo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateUserPhoto(
            @PathVariable("username") String username,
            @RequestPart MultipartFile photo
    ) {
        if (photo == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "multipart file is not provided"
            );
        }

        try {
            userService.updateUserPhoto(username, photo);
        } catch (FileCreationException fileCreationException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "photo file wasn't created", fileCreationException
            );
        }

        return ResponseEntity.ok("photo was successfully updated");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFoundException(UsernameNotFoundException exception) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "user not found", exception
        );
    }
}
