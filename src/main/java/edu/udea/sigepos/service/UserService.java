package edu.udea.sigepos.service;

import edu.udea.sigepos.dto.AuthResponse;
import edu.udea.sigepos.dto.RegisterRequest;
import edu.udea.sigepos.model.Role;
import edu.udea.sigepos.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.udea.sigepos.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.findByCorreo(request.getCorreo()).isPresent()){
            throw new UserService.UserAlreadyExistsException("Correo ya existe.");
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

    public User updateUser(UUID id, User updatedUser) {
        User existingUser = getUserById(id);
        existingUser.setCorreo(updatedUser.getCorreo());
        if(updatedUser.getContrasena() != null && !updatedUser.getContrasena().isEmpty()){
            String encodedPassword = bCryptPasswordEncoder.encode(updatedUser.getContrasena());
            existingUser.setContrasena(encodedPassword);
        }
        existingUser.setRol(updatedUser.getRol());
        return userRepository.save(existingUser);
    }

    public void deleteUser(UUID id) {
        User existingUser = getUserById(id);
        userRepository.delete(existingUser);
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
