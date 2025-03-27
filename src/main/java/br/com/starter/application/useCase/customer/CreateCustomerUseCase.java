package br.com.starter.application.useCase.customer;

import br.com.starter.application.api.customer.dtos.CreateCustomerDTO;
import br.com.starter.domain.address.Address;
import br.com.starter.domain.customer.Customer;
import br.com.starter.domain.customer.CustomerService;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.profile.Profile;
import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserService;  // Serviço para buscar o owner
import br.com.starter.domain.vehicle.Vehicle;
import br.com.starter.domain.vehicle.VehicleService;
import br.com.starter.domain.vehicleType.VehicleType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final GarageService garageService;

    public Optional<Customer> handler(CreateCustomerDTO request, User owner) {

        ModelMapper mapper = new ModelMapper();

        Garage garage = garageService.getByUser(owner).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada!"
            )
        );

        if (request.getDocument() != null)
            UserService.documentValidate(request.getDocument());

        Customer customer = new Customer();
        customer.setStatus(request.getStatus());

        Profile profile = new Profile();
        profile.setDocument(request.getDocument());
        profile.setName(request.getName());
        profile.setPhone(request.getPhone());
        profile.setBirthDate(request.getBirthDate());

        if (request.getAddress() != null) {
            var address = mapper.map(request.getAddress(), Address.class);
            profile.setAddress(address);
        }

        customer.setEmail(request.getEmail());
        customer.setProfile(profile);
        customer.setGarage(garage);
        customer.setOwner(owner);

        Set<Vehicle> vehicles = Optional.ofNullable(request.getVehicles())
            .orElse(Collections.emptyList())
            .stream()
            .map(vehicleRequest -> {
                VehicleType vehicleType = vehicleService.getVehicleTypeById(vehicleRequest.getVehicleTypeId())
                    .orElseThrow(() ->
                        new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Tipo de veículo não encontrado!"
                        )
                    );

                Vehicle vehicle = new Vehicle();
                vehicle.setLicensePlate(vehicleRequest.getLicensePlate());
                vehicle.setColor(vehicleRequest.getColor());
                vehicle.setVehicleType(vehicleType);
                vehicle.setCustomer(customer);
                return vehicle;
            })
            .collect(Collectors.toSet());

        customer.setVehicles(vehicles);

        return Optional.ofNullable(customerService.save(customer));
    }
}
