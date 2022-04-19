package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.User;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.service.UserService;
import bteam.example.ecoolshop.util.JwtTokenUtil;
import bteam.example.ecoolshop.util.MapResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final JwtTokenUtil tokenUtil;

    public UserController(ModelMapper modelMapper, UserService userService, JwtTokenUtil tokenUtil) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto) {
        createOrConvertUser(userDto);

        return ResponseEntity.ok("User is valid");
    }

    @PostMapping("/authorization")
    public ResponseEntity<Map<Object, Object>> authorize(@RequestBody UserDto userDto) {
        try {
            userService.matchThePassword(userDto);

            User convertedUser = createOrConvertUser(userDto);
            String token = tokenUtil.create(convertedUser);

            return ResponseEntity.ok().body(MapResponse.OkResponse("token", token));
        } catch (UserNotFoundException notFoundException) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found", notFoundException);
        } catch (IllegalArgumentException argumentException) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "passwords doesnt match", argumentException);
        }
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Map<Object, Object>> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);

        return ResponseEntity.ok(MapResponse.OkResponse("Deleted:", username));
    }

    @PutMapping("/user")
    public ResponseEntity<Map<Object, Object>> refreshUser(@Valid @RequestBody UserDto userDto) {
        userService.updateUser(userDto);

        return ResponseEntity.ok(MapResponse.OkResponse("Updated:", userDto.getUsername()));
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User createOrConvertUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        try {
            user = userService.getUserById(userService.getIdByUsername(userDto.getUsername()));
        } catch (IllegalArgumentException argumentException) {
            userService.addNewUser(user);
        }

        return user;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
