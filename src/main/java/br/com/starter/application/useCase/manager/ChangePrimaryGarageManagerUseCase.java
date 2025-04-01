package br.com.starter.application.useCase.manager;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import br.com.starter.application.useCase.garage.GetGarageUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.manager.Manager;
import br.com.starter.domain.manager.ManagerService;
import br.com.starter.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChangePrimaryGarageManagerUseCase {
    private final GetGarageUseCase getGarageUseCase;
    private final ManagerService managerService;


    @Transactional
    public Manager handler(User user, UUID garageId) {
        Manager manager = managerService.getByUser(user)
                .map(Manager.class::cast)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager não encontrado"));
        
        Garage garage = getGarageUseCase.handler(garageId)
                .map(Garage.class::cast)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Garagem não encontrada"));
        
        manager.setGarage(garage);     
        return managerService.save(manager);
    }
}