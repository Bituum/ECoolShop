package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.User;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.service.UserService;
import bteam.example.ecoolshop.util.ConversationUtil;
import bteam.example.ecoolshop.util.JwtTokenUtil;
import bteam.example.ecoolshop.util.MapResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bteam.example.ecoolshop.util.ExceptionMapBuilder.getStringStringMap;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;
    private final ConversationUtil<UserDto, User> conversationToUserUtil;
    private final ConversationUtil<User, UserDto> conversationToDtoUtil;

    public UserController(UserService userService, JwtTokenUtil tokenUtil, ConversationUtil<UserDto, User> conversationToUserUtil, ConversationUtil<User, UserDto> conversationToDtoUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
        this.conversationToUserUtil = conversationToUserUtil;
        this.conversationToDtoUtil = conversationToDtoUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto) {
        if (userService.isUsernameExist(userDto.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "username is already exist"
            );
        }
        conversationToUserUtil.createOrConvert(userDto, User.class);

        return ResponseEntity.ok("User is valid");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> authorize(@RequestBody UserDto userDto) {
        try {
            userService.matchThePassword(userDto);

            User convertedUser = conversationToUserUtil.createOrConvert(userDto, User.class);
            String token = tokenUtil.create(convertedUser);

            return ResponseEntity.ok().body(MapResponse.OkResponse("token", token));
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

        return ResponseEntity.ok(MapResponse.OkResponse("Deleted:", username));
    }

    @PutMapping("/")
    public ResponseEntity<Map<Object, Object>> refreshUser(@Valid @RequestBody UserDto userDto) {
        userService.updateUser(userDto);

        return ResponseEntity.ok(MapResponse.OkResponse("Updated:", userDto.getUsername()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return getStringStringMap(ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFoundException(UsernameNotFoundException exception) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "user not found", exception
        );
    }

    //PATCH update password method

}
