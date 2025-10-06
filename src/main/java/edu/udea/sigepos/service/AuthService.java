package edu.udea.sigepos.service;

import edu.udea.sigepos.dto.AuthRequest;
import edu.udea.sigepos.dto.AuthResponse;
import edu.udea.sigepos.dto.RegisterRequest;
import edu.udea.sigepos.model.Role;
import edu.udea.sigepos.model.User;
import edu.udea.sigepos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(AuthRequest request){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContrasena()));
        } catch (Exception e) {
            throw new AuthException("Correo o contraseña incorrectos.");
        }

        User user = userRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new AuthException("Correo no encontrado"));
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .usuario(user)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.findByCorreo(request.getCorreo()).isPresent()){
            throw new UserAlreadyExistsException("Correo ya existe.");
        }

        Role rol = request.getRole() != null ? request.getRole() : Role.USER;

        User user = User.builder()
                .correo(request.getCorreo())
                .contrasena(bCryptPasswordEncoder.encode(request.getContrasena()))
                .rol(rol)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    public static class AuthException extends RuntimeException {
        public AuthException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }
}
