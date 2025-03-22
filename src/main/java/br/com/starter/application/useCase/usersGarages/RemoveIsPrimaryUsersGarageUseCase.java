package br.com.starter.application.useCase.usersGarages;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import br.com.starter.domain.usersGarage.UsersGarage;
import br.com.starter.domain.usersGarage.UsersGarageService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoveIsPrimaryUsersGarageUseCase {

    private final UsersGarageService usersGarageService;

    @Transactional
    public UsersGarage handler(UUID userId, UUID garageId) {
        UsersGarage relation = usersGarageService.getByUserAndGarage(userId, garageId);
        
        if (!relation.isPrimary()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Essa garagem já não é a principal");
        }

        return usersGarageService.update(relation.getId(), false);
    }
}
