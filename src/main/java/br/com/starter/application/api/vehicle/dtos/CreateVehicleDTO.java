package br.com.starter.application.api.vehicle.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateVehicleDTO {
    @NotNull(message = "A placa do veiculo é obrigatória!")
    private String licensePlate;
    @NotNull(message = "O Id do modelo do carro é obrigatório!")
    private UUID vehicleTypeId;
    
    private String year;
    private String color;
    private UUID customerId;
}
