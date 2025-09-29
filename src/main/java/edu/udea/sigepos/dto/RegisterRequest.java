package edu.udea.sigepos.dto;

import edu.udea.sigepos.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String correo;
    private String contrasena;
    private Role role;
}
