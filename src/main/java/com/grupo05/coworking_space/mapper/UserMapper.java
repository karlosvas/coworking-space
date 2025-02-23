package com.grupo05.coworking_space.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.UserDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.model.User;
import com.grupo05.coworking_space.repository.UserRepository;

@Component
public class UserMapper {
    private UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    public User getForeignKey(Integer userFK) {
        Optional<User> user = userRepository.findById(userFK);

        if (user.isPresent())
            return user.get();
        else
            throw new RequestException(ApiError.BAD_REQUEST);
    }
}