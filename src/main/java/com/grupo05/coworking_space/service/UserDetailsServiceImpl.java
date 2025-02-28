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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de gestión de usuarios para la autenticación y autorización.
 * Esta clase proporciona funcionalidades para cargar usuarios por nombre de usuario,
 * registrar nuevos usuarios, iniciar sesión y otras operaciones relacionadas con la
 * gestión de usuarios. También se encarga de crear un usuario administrador por defecto
 * cuando se inicia la aplicación.
 * 
 * @see UserDetailsService Interfaz de Spring Security para cargar usuarios por nombre de usuario.
 * @Slf4j para habilitar el uso de logs en la aplicación.
 * @Service para indicar que es un servicio de Spring.
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${admin.default.password}")
    private String adminDefaultPassword;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Constructor que inyecta todas las dependencias necesarias.
     * 
     * @param userRepository Repositorio para acceder a datos de usuarios
     * @param userMapper Mapper para convertir entre entidades y DTOs
     * @param passwordEncoder Codificador de contraseñas (inyectado de forma lazy)
     * @param jwtUtil Utilidad para manejar tokens JWT
     */
    public UserDetailsServiceImpl(UserRepository userRepository, UserMapper userMapper,
            @Lazy PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

     /**
     * Inicializa el sistema creando un usuario administrador por defecto si no existe.
     * Este método se ejecuta automáticamente después de que se inicialice el bean.
     * @PostConstruct Se ejecuta al iniciar la aplicación
     */
    @PostConstruct
    public void init() {
        try {
            // Creamos el usuairo admin si no existe en la base de datos
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

     /**
     * Carga un usuario por su nombre de usuario para el proceso de autenticación de Spring Security.
     * <p>
     * Implementación requerida por la interfaz UserDetailsService.
     * 
     * @param username Nombre de usuario a buscar
     * @return UserDetails con la información necesaria para la autenticación
     * @throws UsernameNotFoundException Si no se encuentra un usuario con ese nombre
     */
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

     /**
     * Busca un usuario por su ID.
     * 
     * @param id ID del usuario a buscar
     * @return DTO con los datos del usuario encontrado
     * @throws NullPointerException Si no se encuentra un usuario con ese ID
     */
    public UserDTO findUserById(int id) {
        User user = userRepository.findById(id).orElse(null);
        log.info("Usuario encontrado: {}", user.getId());
        return userMapper.convertToDTO(user);
    }

     /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario a buscar
     * @return DTO con los datos del usuario encontrado
     * @throws RequestException Si no se encuentra un usuario con ese nombre
     * @throws RuntimeException Si ocurre otro tipo de error durante la búsqueda
     */
    public UserDTO findByUsername(String username) {
        try {

            Optional<User> user = userRepository.findByUsername(username);
            if (user.isEmpty()) 
                throw new RequestException(ApiError.RECORD_NOT_FOUND);
            log.info("Usuario encontrado: {}", user.get().getId());
            return userMapper.convertToDTO(user.get());
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el usuario: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * <p>
     * Verifica que el email no esté ya registrado, codifica la contraseña y
     * asigna el rol especificado.
     * 
     * @param user DTO con los datos del usuario a registrar
     * @param role Rol que se asignará al nuevo usuario
     * @return DTO con los datos del usuario registrado
     * @throws RequestException Si el email ya está registrado
     * @throws RuntimeException Si ocurre otro tipo de error durante el registro
     */
    public UserDTO registrerUser(UserDTO user, Role role) {
        try {

            if (userRepository.findByEmail(user.getEmail()).isPresent()) 
                throw new RequestException(ApiError.DUPLICATE_EMAIL);

            User userEntity = userMapper.convertToEntity(user);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRole(role);
            userEntity = userRepository.save(userEntity);
            log.info("Usuario registrado: {}", userEntity.getId());
            return userMapper.convertToDTO(userEntity);
        } catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    /**
     * Autentica un usuario y genera un token JWT.
     * <p>
     * Verifica las credenciales del usuario (email y contraseña) y, si son correctas,
     * genera un token JWT para el acceso a recursos protegidos.
     * 
     * @param userDTO DTO con el email y contraseña del usuario
     * @return DTO del usuario con el token JWT generado
     * @throws RequestException Si el usuario no existe o las credenciales son incorrectas
     * @throws RuntimeException Si ocurre otro tipo de error durante la autenticación
     */
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
            log.info("Usuario logeado: {}", user.getId());
            return loggedUserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema.
     * 
     * @return Lista de DTOs con los datos de todos los usuarios
     * @throws RuntimeException Si ocurre algún error durante la búsqueda
     */
    public List<UserDTO> findAllUser() {
        try {
            List<User> user = userRepository.findAll();
            log.info("Se han encontrado {} usuarios", user.size());
            return user.stream()
                    .map(userMapper::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar los usuarios: " + e.getMessage());
        }
    }

    /**
     * Elimina un usuario del sistema por su ID.
     * 
     * @param id ID del usuario a eliminar
     * @return DTO con los datos del usuario eliminado
     * @throws RequestException Si no se encuentra el usuario con el ID proporcionado
     * @throws RuntimeException Si ocurre otro tipo de error durante la eliminación
     */
    public UserDTO deleteUserByID(int id) {
        try {
            Optional<User> user = userRepository.findById(id);

            if (user.isEmpty()) 
                throw new RequestException(ApiError.RECORD_NOT_FOUND);

            userRepository.deleteById(id);
            log.info("Usuario eliminado: {}", user.get().getId());
            return userMapper.convertToDTO(user.get());
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad referencial al eliminar usuario: {}", e.getMessage());
            throw new RequestException(ApiError.ASSOCIATED_RESOURCES);
    }    catch (RequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}
