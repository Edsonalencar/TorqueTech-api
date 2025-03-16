package br.com.starter.application.useCase.vehicle;

import br.com.starter.application.api.vehicle.dtos.UpdateVehicleDTO;
import br.com.starter.domain.vehicle.Vehicle;
import br.com.starter.domain.vehicle.VehicleService;
import br.com.starter.domain.user.UserService;
import br.com.starter.domain.vehicleType.VehicleType;
import br.com.starter.domain.vehicleType.VehicleTypeCategory;
import br.com.starter.domain.vehicleType.VehicleTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateVehicleUseCase {
    private final VehicleService vehicleService;
    private final VehicleTypeService vehicleTypeService;
    private final UserService userService;

    @Transactional
    public Optional<Vehicle> handler(UUID vehicleId, UpdateVehicleDTO request) {
        var vehicle = vehicleService.getById(vehicleId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Veículo não encontrado!"
                )
        );

        if (request.getVehicleTypeId() != null) {
            var vehicleType = vehicleTypeService.findById(request.getVehicleTypeId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Tipo de veículo não encontrado")
                    );
            vehicle.setVehicleType(vehicleType);
        }
        
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setColor(request.getColor());
        vehicle.setYear(request.getYear());

        return Optional.of(vehicleService.save(vehicle));
    }
}
