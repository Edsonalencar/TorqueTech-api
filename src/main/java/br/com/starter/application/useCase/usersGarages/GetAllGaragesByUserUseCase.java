package br.com.starter.application.useCase.usersGarages;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import br.com.starter.domain.usersGarage.UsersGarage;
import br.com.starter.domain.usersGarage.UsersGarageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllGaragesByUserUseCase {

    private final UsersGarageService usersGarageService;

    @Transactional
    public List<UsersGarage> handler(UUID userId) {
        List<UsersGarage> garages = usersGarageService.findByUser(userId);

        if (garages.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Usuário não possui garagens vinculadas ou não existe");
        }

        return garages;
    }
}