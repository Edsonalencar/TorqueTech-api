package br.com.starter.application.useCase.vehicleType;

import br.com.starter.application.api.vehicleType.dtos.UpdateVehicleTypeDTO;
import br.com.starter.domain.vehicleType.VehicleType;
import br.com.starter.domain.vehicleType.VehicleTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateVehicleTypeUseCase {

    private final VehicleTypeService vehicleTypeService;

    @Transactional
    public VehicleType handler(UUID vehicleTypeId, UpdateVehicleTypeDTO request) {
        VehicleType vehicleType = vehicleTypeService.getById(vehicleTypeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de veículo não encontrado com o ID: " + vehicleTypeId
                ));

        vehicleType.setModel(request.getModel());
        vehicleType.setBrand(request.getBrand());
        vehicleType.setCategory(request.getCategory());

        return vehicleTypeService.save(vehicleType);
    }
}
