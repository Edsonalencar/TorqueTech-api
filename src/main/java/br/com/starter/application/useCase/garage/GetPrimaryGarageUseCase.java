package br.com.starter.application.useCase.garage;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.starter.application.useCase.usersGarages.GetPrimaryGarageByUserUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.user.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetPrimaryGarageUseCase {
    private final GetPrimaryGarageByUserUseCase getPrimaryGarageByUserUseCase;
    private final UserService userService;

    public Garage handler(UUID userId) {
        // Verifica se o usuário existe
        userService.getUserById(userId);
        
        return getPrimaryGarageByUserUseCase.handler(userId);
    }
}