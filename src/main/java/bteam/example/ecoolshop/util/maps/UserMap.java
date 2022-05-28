package bteam.example.ecoolshop.util.maps;

import bteam.example.ecoolshop.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserMap {
    private final Map<String, UserDto> userDtoMap = new HashMap<String, UserDto>();

    public void put(String key, UserDto uSerDto) {
        userDtoMap.put(key, uSerDto);
    }

    public Optional<UserDto> get(String key) {
        return Optional.of(userDtoMap.get(key));
    }
}
