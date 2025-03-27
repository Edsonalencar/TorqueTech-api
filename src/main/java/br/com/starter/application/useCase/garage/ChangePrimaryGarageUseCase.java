package br.com.starter.application.useCase.garage;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.Remove;
import org.springframework.stereotype.Component;

import br.com.starter.application.useCase.manager.ChangePrimaryGarageManagerUseCase;
import br.com.starter.application.useCase.mechanic.ChangePrimaryGarageMechanicUseCase;
import br.com.starter.application.useCase.usersGarages.RemoveIsPrimaryUsersGarageUseCase;
import br.com.starter.application.useCase.usersGarages.SetIsPrimaryUsersGarageUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.role.RoleType;
import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserService;
import br.com.starter.domain.usersGarage.UsersGarage;
import br.com.starter.domain.usersGarage.UsersGarageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangePrimaryGarageUseCase {
    private final UsersGarageService usersGarageService;
    private final SetIsPrimaryUsersGarageUseCase setIsPrimaryUsersGarageUseCase;
    private final RemoveIsPrimaryUsersGarageUseCase  removeIsPrimaryUsersGarageUseCase;
    private final UserService userService;
    private final ChangePrimaryGarageManagerUseCase changePrimaryGarageManagerUseCase;
    private final ChangePrimaryGarageMechanicUseCase changePrimaryGarageMechanicUseCase;

    @Transactional
    public UsersGarage handler(UUID userId, UUID garageId) {
        // Verifica se o usuário existe
        User user = userService.getUserById(userId);


        // Busca a garagem principal do user
        UsersGarage usersGarage = usersGarageService.findPrimaryByUser(userId);

        // Remove a designação de garagem primária da garagem atual do usuário
        removeIsPrimaryUsersGarageUseCase.handler(userId, usersGarage.getGarage().getId());        
        if (user.getRole().getName().equals("ROLE_MANAGER")) {
            changePrimaryGarageManagerUseCase.handler(user, garageId);
        }
        if (user.getRole().getName().equals("ROLE_USER")) {
            changePrimaryGarageMechanicUseCase.handler(user, garageId);
        }
        return setIsPrimaryUsersGarageUseCase.handler(userId, garageId);
    }
}