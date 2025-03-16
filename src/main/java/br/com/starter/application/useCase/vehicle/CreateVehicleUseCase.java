package br.com.starter.application.useCase.vehicle;

import br.com.starter.application.api.vehicle.dtos.CreateVehicleDTO;
import br.com.starter.domain.customer.Customer;
import br.com.starter.domain.customer.CustomerService;
import br.com.starter.domain.user.User;
import br.com.starter.domain.vehicle.Vehicle;
import br.com.starter.domain.vehicle.VehicleService;
import br.com.starter.domain.vehicleType.VehicleType;
import br.com.starter.domain.vehicleType.VehicleTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateVehicleUseCase {

    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final VehicleTypeService vehicleTypeService;

    @Transactional
    public Vehicle handler(CreateVehicleDTO request, User user) {
        Optional<Customer> customer = customerService.getById(request.getCustomerId());
        if (customer.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cliente não encontrado!"
            );
        }

        VehicleType vehicleType = vehicleTypeService.getVehicleTypeById(request.getVehicleTypeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Tipo de veículo não encontrado com o ID: " + request.getVehicleTypeId()
                ));


        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setColor(request.getColor());
        vehicle.setYear(request.getYear());
        vehicle.setVehicleType(vehicleType);
        vehicle.setCustomer(customer.get());

        return vehicleService.save(vehicle);
    }
}


