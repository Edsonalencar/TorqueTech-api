package br.com.starter.application.useCase.mechanic;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import br.com.starter.application.useCase.garage.GetGarageUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.mechanic.Mechanic;
import br.com.starter.domain.mechanic.MechanicService;
import br.com.starter.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangePrimaryGarageMechanicUseCase {
    private final GetGarageUseCase getGarageUseCase;
    private final MechanicService mechanicService;

    @Transactional
    public Mechanic handler(User user, UUID garageId) {
        Mechanic mechanic = mechanicService.getByUser(user)
                .map(Mechanic.class::cast)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mecânico não encontrado"));
        
        Garage garage = getGarageUseCase.handler(garageId)
                .map(Garage.class::cast)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Garagem não encontrada"));
        
        mechanic.setGarage(garage);
        
        return mechanicService.save(mechanic);
    }
}