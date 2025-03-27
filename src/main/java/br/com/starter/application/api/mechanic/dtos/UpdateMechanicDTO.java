package br.com.starter.application.api.mechanic.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateMechanicDTO {
    private String username;
    private String password;
    private String name;
    private String document;
    private String phone;
    private LocalDate birthDate = null;
}
