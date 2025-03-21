package br.com.starter.application.useCase.usersGarages;

import br.com.starter.application.api.usersGarage.dtos.CreateUsersGarageDTO;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserService;
import br.com.starter.domain.usersGarage.UsersGarage;
import br.com.starter.domain.usersGarage.UsersGarageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateUsersGarageUseCase {

    private final UsersGarageService usersGarageService;
    private final UserService userService;
    private final GarageService garageService;

    @Transactional
    public Optional<UsersGarage> handler(CreateUsersGarageDTO request) {
        UUID userId = request.getUserId();
        UUID garageId = request.getGarageId();

        User user = userService.getUserById(userId);
        Garage garage = garageService.getById(garageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Garagem não encontrada"));

        if (usersGarageService.isUserGarage(userId, garageId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Essa relação já existe");
        }
        if (request.isPrimary() && usersGarageService.userAlreadyHasPrimaryGarage(userId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Usuário já possui uma garagem principal");
        }
        

        UsersGarage usersGarage = new UsersGarage();
        usersGarage.setUser(user);
        usersGarage.setGarage(garage);
        usersGarage.setPrimary(request.isPrimary());;
        usersGarage.setCreatedAt(LocalDateTime.now());
        usersGarage.setIsPrimaryEdit(LocalDateTime.now());

        return Optional.of(usersGarageService.save(usersGarage));
    }
}
