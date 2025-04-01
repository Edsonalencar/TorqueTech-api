package br.com.starter.application.useCase.usersGarages;

import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.usersGarage.UsersGarage;
import br.com.starter.domain.usersGarage.UsersGarageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetPrimaryGarageByUserUseCase {

    private final UsersGarageService usersGarageService;

    @Transactional
    public Garage handler(UUID userId) {
        UsersGarage primaryRelation = usersGarageService.findPrimaryByUser(userId);
        return primaryRelation.getGarage();
    }
}