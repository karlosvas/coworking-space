package com.grupo05.coworking_space.service;

import com.grupo05.coworking_space.dto.UserDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.enums.Role;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.mapper.UserMapper;
import com.grupo05.coworking_space.model.User;
import com.grupo05.coworking_space.repository.UserRepository;
import com.grupo05.coworking_space.utils.JwtUtil;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${admin.default.password}")
    private String adminDefaultPassword;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserDetailsServiceImpl(UserRepository userRepository, UserMapper userMapper,
            @Lazy PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void init() {
        try {
            if (userRepository.findByRole(Role.ROLE_ADMIN).isEmpty()) {
                log.info("Creating admin user...");
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@coworking.com");
                admin.setPassword(passwordEncoder.encode(adminDefaultPassword));
                admin.setRole(Role.ROLE_ADMIN);
                userRepository.save(admin);
                log.info("Admin user created successfully");
            } else {
                log.info("Admin user already exists");
            }
        } catch (Exception e) {
            log.error("Error creating admin user: {}", e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();
    }

    public UserDTO findUserById(int id) {
        User user = userRepository.findById(id).orElse(null);
        return userMapper.convertToDTO(user);
    }

    public UserDTO findByUsername(String username) {
        try {

            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new RequestException(ApiError.RECORD_NOT_FOUND);
            }
            return userMapper.convertToDTO(user.get());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el usuario: " + e.getMessage());
        }
    }

    public UserDTO registrerUser(UserDTO user, Role role) {
        try {

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new RequestException(ApiError.DUPLICATE_EMAIL);
            }

            User userEntity = userMapper.convertToEntity(user);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRole(role);
            userEntity = userRepository.save(userEntity);

            return userMapper.convertToDTO(userEntity);
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public UserDTO loginUser(UserDTO userDTO) {
        try {
            // Obtenemos el usuario de la base de datos
            Optional<User> optionalUser = userRepository.findByEmail(userDTO.getEmail());

            if (optionalUser.isEmpty())
                throw new RequestException(ApiError.RECORD_NOT_FOUND);

            // Verificamos que la contraseña sea correcta, comparando la contraseña
            // encriptada con la contraseña ingresada
            // por el usuario encodeada
            User user = optionalUser.get();
            // Verificar que las contraseñas y emails coinciden coincidan
            // In the loginUser method:
            if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword()))
                throw new RequestException(ApiError.AUTHENTICATION_FAILED);

            // Generamos el token usando el usuario encontrado
            String token = jwtUtil.generateToken(user);

            // Actualizamos el token
            UserDTO loggedUserDTO = userMapper.convertToDTO(user);
            loggedUserDTO.setToken(token);
            return loggedUserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public List<UserDTO> findAllUser() {
        try {
            List<User> user = userRepository.findAll();
            return user.stream()
                    .map(userMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar los usuarios: " + e.getMessage());
        }
    }

    public UserDTO deleteUserByID(int id) {
        try {
            Optional<User> user = userRepository.findById(id);

            if (user.isEmpty()) {
                throw new RequestException(ApiError.RECORD_NOT_FOUND);
            }

            userRepository.deleteById(id);
            return userMapper.convertToDTO(user.get());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}
