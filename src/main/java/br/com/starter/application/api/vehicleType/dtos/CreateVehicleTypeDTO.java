package br.com.starter.application.api.vehicleType.dtos;

import br.com.starter.domain.vehicleType.VehicleTypeCategory;
import lombok.Data;

@Data
public class CreateVehicleTypeDTO {
    private String model;
    private String brand;
    private VehicleTypeCategory category;
}
