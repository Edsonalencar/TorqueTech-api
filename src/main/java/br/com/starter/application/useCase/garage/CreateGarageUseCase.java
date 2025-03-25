package br.com.starter.application.useCase.garage;

import br.com.starter.application.api.garage.dtos.CreateGarageDTO;
import br.com.starter.application.api.garage.dtos.CreateGarageForExistingUsersDTO;
import br.com.starter.application.api.user.dto.UserRegistrationRequest;
import br.com.starter.application.api.usersGarage.dtos.CreateUsersGarageDTO;
import br.com.starter.application.useCase.usersGarages.CreateUsersGarageUseCase;
import br.com.starter.domain.address.Address;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateGarageUseCase {

    private final GarageService garageService;
    private final UserService userService;
    private final CreateUsersGarageUseCase createUsersGarageUseCase;

    @Transactional
    public Optional<Garage> handler(CreateGarageDTO request) {
        ModelMapper mapper = new ModelMapper();

        var garage = new Garage();
        garage.setName(request.getName());
        garage.setCnpj(request.getCnpj());

        if (request.getAddress() != null) {
            var address = mapper.map(request.getAddress(), Address.class);
            garage.setAddress(address);
        }

        // Garantindo que os dados obrigatórios do owner foram enviados
        if (request.getUsername() == null|| request.getDocument() == null ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e documentos do proprietário são obrigatórios");
        }

        User user;

        var createUserRequest = mapper.map(request, UserRegistrationRequest.class);
        createUserRequest.setName(request.getOwnerName());
        user = userService.create(createUserRequest);

        garage.setOwner(user);
        Garage savedGarage = garageService.save(garage);

        // Cria relação UsersGarage
        var usersGarageRequest = new CreateUsersGarageDTO();
        usersGarageRequest.setUserId(user.getId());
        usersGarageRequest.setGarageId(savedGarage.getId());
        usersGarageRequest.setPrimary(true);

        createUsersGarageUseCase.handler(usersGarageRequest);

        return Optional.of(savedGarage);
    }


    @Transactional
    public Optional<Garage> handlerForExistingUsers(CreateGarageForExistingUsersDTO request) {
        ModelMapper mapper = new ModelMapper();

        var garage = new Garage();
        garage.setName(request.getName());
        garage.setCnpj(request.getCnpj());

        if (request.getAddress() != null) {
            var address = mapper.map(request.getAddress(), Address.class);
            garage.setAddress(address);
        }

        // Garantindo que os dados obrigatórios do owner foram enviados
        if (request.getUsername() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email do proprietário é obrigatório");
        }

        User user;
        var existingUser = userService.findByUsername(request.getUsername());

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
        }

        garage.setOwner(user);
        Garage savedGarage = garageService.save(garage);

        // Cria relação UsersGarage
        var usersGarageRequest = new CreateUsersGarageDTO();
        usersGarageRequest.setUserId(user.getId());
        usersGarageRequest.setGarageId(savedGarage.getId());
        usersGarageRequest.setPrimary(false);

        createUsersGarageUseCase.handler(usersGarageRequest);

        return Optional.of(savedGarage);
    }
}
