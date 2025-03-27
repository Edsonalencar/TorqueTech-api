package br.com.starter.application.api.mechanic.dtos;

import br.com.starter.domain.role.RoleType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateMechanicDTO {
    private String username;
    private String password;

    private String name;
    private String document;
    private String phone;
    private LocalDate birthDate = null;
    private RoleType role = RoleType.ROLE_USER;
}
