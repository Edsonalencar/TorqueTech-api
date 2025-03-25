package br.com.starter.application.useCase.garage;

import br.com.starter.application.useCase.usersGarages.GetAllGaragesByUserUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserService;
import br.com.starter.domain.usersGarage.UsersGarage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAllGaragesUseCase {

    private final UserService userService;
    private final GetAllGaragesByUserUseCase getAllGaragesByUserUseCase;

    @Transactional
    public List<Garage> handler(UUID userId) {
        // Verifica se o usuário existe
        userService.getUserById(userId);

        // Busca as relações UsersGarage associadas ao usuário
        List<UsersGarage> usersGarageList = getAllGaragesByUserUseCase.handler(userId);

        // Mapeia para uma lista de Garage
        return usersGarageList.stream()
                .map(UsersGarage::getGarage)
                .collect(Collectors.toList());
    }
}
