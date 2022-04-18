package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.dto.UserDto;
import bteam.example.ecoolshop.entity.User;
import bteam.example.ecoolshop.exception.UserNotFoundException;
import bteam.example.ecoolshop.repository.IRoleRepository;
import bteam.example.ecoolshop.repository.IUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void addNewUser(UserDto userDto) {
    }

    public void removeUserById(int id) {
        userRepository.deleteById(id);
    }

    public void editUserById() {

    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("wrong user_id!")
        );
    }

    public int getIdByUsername(String username) {
        return userRepository.findIdByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("wrong username!")
        );
    }

    public void matchThePassword(UserDto userDto) {
        User userByUsername = userRepository
                .findUserByUsername(userDto.getUsername())
                .orElseThrow(
                        UserNotFoundException::new
                );

        if (!bCryptPasswordEncoder.matches(new String(userDto.getPassword()), new String(userByUsername.getPassword()))) {
            throw new IllegalArgumentException("password are not the same!");
        }
    }

}
