package br.com.starter.application.useCase.manager;

import br.com.starter.application.api.manager.dtos.CreateManagerDTO;
import br.com.starter.application.api.user.dto.UserRegistrationRequest;
import br.com.starter.application.api.usersGarage.dtos.CreateUsersGarageDTO;
import br.com.starter.application.useCase.usersGarages.CreateUsersGarageUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.manager.Manager;
import br.com.starter.domain.manager.ManagerService;
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
public class CreateManagerUseCase {
    private final UserService userService;
    private final ManagerService managerService;
    private final GarageService garageService;
    private final CreateUsersGarageUseCase createUsersGarageUseCase;

    @Transactional
    public Optional<Manager> handler(CreateManagerDTO request, User user) {
        ModelMapper mapper = new ModelMapper();

        Garage garage = garageService.getByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O usuário não possui uma oficina registrada"));

        var createUserRequest = mapper.map(request, UserRegistrationRequest.class);
        var newUser = userService.create(createUserRequest);

        // Cria a relação como primária
        var createUsersGarageRequest = new CreateUsersGarageDTO();
        createUsersGarageRequest.setUserId(newUser.getId());
        createUsersGarageRequest.setGarageId(garage.getId());
        createUsersGarageRequest.setPrimary(true);

        createUsersGarageUseCase.handler(createUsersGarageRequest)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar relação user garage"));

        var manager = new Manager();
        manager.setUser(newUser);
        manager.setGarage(garage);

        return Optional.of(managerService.save(manager));
    }

}
