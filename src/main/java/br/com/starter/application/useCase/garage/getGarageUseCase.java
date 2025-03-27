package br.com.starter.application.useCase.garage;

import br.com.starter.domain.garage.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetGarageUseCase {
    private final GarageService garageService;

    public Optional<?> handler(UUID garageId) {
        return garageService.getById(garageId);
    }
}
