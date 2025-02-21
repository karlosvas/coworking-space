package com.grupo05.coworking_space.controller;

import com.grupo05.coworking_space.model.Usuario;
import com.grupo05.coworking_space.repository.UserRepository;
import com.grupo05.coworking_space.service.UserDetailsServiceImpl;
import com.grupo05.coworking_space.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario){
        Optional<Usuario> usuarioOptional = userRepository.findByUsername(usuario.getUsername());
        if(!usuarioOptional.isEmpty()){
            return ResponseEntity.ok("Ya estas registrado");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        userRepository.save(usuario);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUsuario(@RequestBody Usuario usuarioRequest) {

        Optional<Usuario> usuarioOptional = userRepository.findByUsername(usuarioRequest.getUsername());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(usuarioRequest.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        String token = jwtUtil.generateToken(usuario);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/resources")
    public String home(){
        return "Home";
    }

}
