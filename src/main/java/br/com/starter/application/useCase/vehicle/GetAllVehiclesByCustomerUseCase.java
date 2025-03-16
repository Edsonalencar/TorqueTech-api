package br.com.starter.application.useCase.vehicle;

import br.com.starter.domain.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetAllVehiclesByCustomerUseCase {

    private final VehicleService vehicleService;

    public Optional<?> handler(UUID customerId) {
        return Optional.of(
            vehicleService.findAllByCustomer(customerId)
        );
    }
}
