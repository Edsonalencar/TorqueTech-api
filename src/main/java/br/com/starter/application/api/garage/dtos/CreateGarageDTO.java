package br.com.starter.application.api.garage.dtos;

import br.com.starter.application.api.common.AddressDTO;
import br.com.starter.domain.role.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateGarageDTO {
    private String name;
    private String cnpj;

    @Email(message = "O username deve ser um email válido")
    @NotBlank(message = "O email é obrigatório")
    private String username;
    @NotBlank(message = "A senha é obrigatória")
    private String password;

    @NotBlank(message = "O nome é obrigatório")
    private String ownerName;
    private String document;
    private String phone;
    private LocalDate birthDate = null;
    private RoleType role = RoleType.ROLE_ADMIN;

    private AddressDTO address = null;
}
