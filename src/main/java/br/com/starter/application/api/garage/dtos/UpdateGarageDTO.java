package br.com.starter.application.api.garage.dtos;

import br.com.starter.application.api.common.AddressDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateGarageDTO {
    private String name;
    private String cnpj;
    private String username;
    private String password;
    private String ownerName;
    private UUID ownerId;
    private String document;
    private String phone;
    private LocalDate birthDate = null;
    private AddressDTO address = null;
}
