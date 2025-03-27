package br.com.starter.application.api.garage.dtos;

import br.com.starter.application.api.common.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CreateGarageForExistingUsersDTO {
    private String name;
    private String cnpj;

    @Email(message = "O username deve ser um email válido")
    @NotBlank(message = "O email é obrigatório")
    private String username;
    private AddressDTO address = null;
}
