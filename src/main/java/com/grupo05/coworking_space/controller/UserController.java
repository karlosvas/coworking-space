package com.grupo05.coworking_space.controller;

import com.grupo05.coworking_space.model.User;
import com.grupo05.coworking_space.repository.UserRepository;
import com.grupo05.coworking_space.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@RequestBody User user){
        Optional<User> usuarioOptional = userRepository.findByUsername(user.getUsername());
        if(!usuarioOptional.isEmpty()){
            return ResponseEntity.ok("Ya estas registrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody User userRequest) {

        Optional<User> usuarioOptional = userRepository.findByUsername(userRequest.getUsername());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(401).body("User no encontrado");
        }

        User user = usuarioOptional.get();

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/resources")
    public String home(){
        return "Home";
    }

}
