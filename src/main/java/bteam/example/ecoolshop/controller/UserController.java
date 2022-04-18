package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.Role;
import bteam.example.ecoolshop.entity.User;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.service.RoleService;
import bteam.example.ecoolshop.service.UserService;
import bteam.example.ecoolshop.util.JwtTokenUtil;
import bteam.example.ecoolshop.util.MapResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtTokenUtil tokenUtil;

    public UserController(ModelMapper modelMapper, UserService userService, RoleService roleService, JwtTokenUtil tokenUtil) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.roleService = roleService;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto) {
        User user = convertToUser(userDto);

        return ResponseEntity.ok("User is valid");
    }

    @PostMapping("/authorization")
    public ResponseEntity<Map<Object, Object>> authorize(@Valid @RequestBody UserDto userDto) {
        try {
            userService.matchThePassword(userDto);

            User convertedUser = convertToUser(userDto);
            String token = tokenUtil.create(convertedUser);

            return ResponseEntity.status(HttpStatus.OK).body(MapResponse.OkResponse("token", token));
        } catch (UsernameNotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MapResponse.errorResponse("username not found"));
        } catch (IllegalArgumentException argumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MapResponse.errorResponse("wrong password"));
        }
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();

        errors.put("ERROR:", ex.getMessage());
        return errors;
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        try {
            user = userService.getUserById(userService.getIdByUsername(userDto.getUsername()));
        } catch (IllegalArgumentException argumentException) {
            Set<Role> userRole = new HashSet<>();
            user.setUser_id(0);
            userRole.add(roleService.getRoleById(1));
            user.setUserRole(userRole);
        }
        //TODO refactor: move that logic from controller to the user-service layer
        return user;
    }

}
