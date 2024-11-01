package com.npichuzhkin.JWTTask.utils;

import com.npichuzhkin.JWTTask.dto.UserDTO;
import com.npichuzhkin.JWTTask.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO mapToDTO(User u){
        return new UserDTO(u.getUsername(),
                            u.getPassword(),
                            u.getRole(),
                            u.isNonLocked(),
                            u.getFailedLoginAttempts(),
                            u.getLastFailedLogin());
    }

    public User mapToModel(UserDTO dto){
        return new User(dto.getUsername(),
                        dto.getPassword(),
                        dto.getRole(),
                        dto.isNonLocked(),
                        dto.getFailedLoginAttempts(),
                        dto.getLastFailedLogin());
    }
}
