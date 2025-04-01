package br.com.starter.application.useCase.mechanic;

import br.com.starter.application.api.mechanic.dtos.CreateMechanicDTO;
import br.com.starter.application.api.user.dto.UserRegistrationRequest;
import br.com.starter.application.api.usersGarage.dtos.CreateUsersGarageDTO;
import br.com.starter.application.useCase.usersGarages.CreateUsersGarageUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.mechanic.Mechanic;
import br.com.starter.domain.mechanic.MechanicService;
import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateMechanicUseCase {

    private final UserService userService;
    private final MechanicService mechanicService;
    private final GarageService garageService;
    private final CreateUsersGarageUseCase createUsersGarageUseCase;

    @Transactional
    public Optional<Mechanic> handler(CreateMechanicDTO request, User user) {
        ModelMapper mapper = new ModelMapper();

        Garage garage = garageService.getByUser(user).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada"));

        var createUserRequest = mapper.map(request, UserRegistrationRequest.class);
        var newUser = userService.create(createUserRequest);

        // Criação da relação UsersGarage como principal
        var createUsersGarageRequest = new CreateUsersGarageDTO();
        createUsersGarageRequest.setUserId(newUser.getId());
        createUsersGarageRequest.setGarageId(garage.getId());
        createUsersGarageRequest.setPrimary(true);

        createUsersGarageUseCase.handler(createUsersGarageRequest)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar relação user garage"));

        var mechanic = new Mechanic();
        mechanic.setUser(newUser);
        mechanic.setGarage(garage);

        return Optional.of(mechanicService.save(mechanic));
    }
}
