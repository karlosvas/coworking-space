package com.grupo05.coworking_space.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;

import com.grupo05.coworking_space.dto.UserDTO;
import com.grupo05.coworking_space.enums.ApiError;
import com.grupo05.coworking_space.exception.RequestException;
import com.grupo05.coworking_space.model.User;
import com.grupo05.coworking_space.repository.UserRepository;

/**
 * Clase encargada de convertir entre entidades de User y sus correspondientes DTOs.
 * Este componente facilita la transformación bidireccional entre los objetos de dominio
 * User y los objetos de transferencia de datos UserDTO, permitiendo
 * separar la capa de persistencia de la capa de presentación.
 */
@Component
public class UserMapper {
    private UserRepository userRepository;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * 
     * @param userRepository Componente para mapear entidades User
     */
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Convierte una entidad User a su correspondiente objeto DTO.
     * Este método extrae los datos relevantes de la entidad y los encapsula
     * en un objeto DTO para su transferencia a la capa de presentación.
     * 
     * @param user Entidad de usuario a convertir
     * @return Objeto DTO con los datos del usuario
     */
    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    /**
     * Convierte un objeto DTO a su correspondiente entidad User.
     * Este método crea una nueva entidad User basada en los datos
     * proporcionados por el DTO. También resuelve las referencias a entidades
     * relacionadas como User.
     * 
     * @param userDTO DTO de usuario a convertir
     * @return Entidad User con los datos del DTO
     */
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        return user;
    }

    /**
     * Obtiene la entidad User correspondiente a una clave foránea.
     * Este método busca en la base de datos la entidad User cuya clave
     * primaria coincide con la clave foránea proporcionada. Si no se
     * encuentra ninguna entidad, se lanza una excepción.
     * 
     * @param userFK Clave foránea de la entidad User a buscar en la base de datos.
     * @return Entidad User correspondiente a la clave foránea proporcionada.
     * @throws RequestException Si no se encuentra ninguna entidad con la clave foránea proporcionada.
     */
    public User getForeignKey(Integer userFK) {
        Optional<User> user = userRepository.findById(userFK);

        if (user.isPresent())
            return user.get();
        else
            throw new RequestException(ApiError.BAD_REQUEST);
    }
}