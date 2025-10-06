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



    public static class AuthException extends RuntimeException {
        public AuthException(String message) {
            super(message);
        }
    }

}
