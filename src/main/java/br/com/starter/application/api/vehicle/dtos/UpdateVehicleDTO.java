package br.com.starter.application.api.vehicle.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateVehicleDTO {
    private UUID vehicleTypeId;
    private String licensePlate;
    private String color;
    private String year;
}
