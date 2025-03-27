package br.com.starter.application.useCase.usersGarages;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.usersGarage.UsersGarage;
import br.com.starter.domain.usersGarage.UsersGarageService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SetIsPrimaryUsersGarageUseCase {

    private final UsersGarageService usersGarageService;
    private final GarageService garageService;

    @Transactional
    public UsersGarage handler(UUID userId, UUID garageId) {
        UsersGarage relation = usersGarageService.getByUserAndGarage(userId, garageId);

        // Verifica se a garagem existe
        garageService.getById(garageId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Garagem não encontrada"));

        if (relation.isPrimary()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Essa garagem já é a principal");
        }

        return usersGarageService.update(relation.getId(), true);
    }
}