package br.com.starter.application.api.usersGarage.dtos;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUsersGarageDTO {
    private UUID userId;
    private UUID garageId;
    private boolean isPrimary;
}
