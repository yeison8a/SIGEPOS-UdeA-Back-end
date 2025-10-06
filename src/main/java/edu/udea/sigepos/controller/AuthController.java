package edu.udea.sigepos.controller;

import edu.udea.sigepos.dto.AuthRequest;
import edu.udea.sigepos.dto.AuthResponse;
import edu.udea.sigepos.dto.RegisterRequest;
import edu.udea.sigepos.service.AuthService;
import edu.udea.sigepos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }



}
