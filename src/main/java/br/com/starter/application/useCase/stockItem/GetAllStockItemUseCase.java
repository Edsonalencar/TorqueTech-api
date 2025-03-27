package br.com.starter.application.useCase.stockItem;

import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.stockItem.StockItemService;
import br.com.starter.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetAllStockItemUseCase {
    private final StockItemService stockItemService;
    private final GarageService garageService;

    public Optional<?> handler(
        User user
    ) {
        var garage = garageService.getByUser(user).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada"
            )
        );

        return Optional.of(stockItemService.getAllByGarage(
            garage.getId()
        ));
    }
}
