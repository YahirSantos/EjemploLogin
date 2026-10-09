package mx.edu.fesaragon.login.controller;

import mx.edu.fesaragon.login.dto.RegisterRequest;
import mx.edu.fesaragon.login.dto.LoginRequest;
import mx.edu.fesaragon.login.model.User;
import mx.edu.fesaragon.login.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")

public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody RegisterRequest request){
        try{
            userService.register(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario registrado correctamente."));
        } catch (IllegalArgumentException exception){
            return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginRequest request) {

        if (request == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error",
                            "La solicitud de inicio de sesión es obligatoria."
                    ));
        }

        boolean authenticated = userService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        if (!authenticated) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error",
                            "Usuario o contraseña incorrectos."
                    ));
        }

        return ResponseEntity
                .ok()
                .body(Map.of(
                        "message",
                        "Inicio de sesión correcto."
                ));
    }
}
