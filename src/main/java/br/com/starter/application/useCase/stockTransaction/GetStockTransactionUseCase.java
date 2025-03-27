package br.com.starter.application.useCase.stockTransaction;

import br.com.starter.application.api.stockTransaction.dtos.StockTransactionDTO;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.stockTransaction.StockTransactionService;
import br.com.starter.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetStockTransactionUseCase {
    private final StockTransactionService stockTransactionService;
    private final GarageService garageService;

    public Optional<?> handler(
            User user,
            UUID stockTransactionId
    ) {
        Garage garage = garageService.getByUser(user).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O usuário não possui uma oficina registrada!"
                )
        );

        var stockTransaction = stockTransactionService.getByIdAndGarageId(
            stockTransactionId,
            garage.getId()
        );

        if (!stockTransaction.isPresent())
            return Optional.empty();

        return Optional.of(new StockTransactionDTO(stockTransaction.orElseThrow()));
    }
}
