package br.com.starter.application.useCase.stockTransaction;

import br.com.starter.application.api.stockTransaction.dtos.OutputStockTransactionRequest;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.stockItem.StockItemService;
import br.com.starter.domain.stockTransaction.StockTransaction;
import br.com.starter.domain.stockTransaction.StockTransactionService;
import br.com.starter.domain.stockTransaction.TransactionStatus;
import br.com.starter.domain.stockTransaction.TransactionType;
import br.com.starter.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CancelStockTransactionUseCase {
    private final StockTransactionService stockTransactionService;
    private final StockItemService stockItemService;
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
        ).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Essa movimentação de estoque não pode ser encontrada!"
            )
        );

        var stockItem = stockItemService.findById(
            stockTransaction.getStockItem().getId(),
            garage
        ).orElseThrow();

        stockItem.setQuantity(stockItem.getQuantity() + stockTransaction.getQuantity());
        stockItemService.save(stockItem);

        stockTransaction.setStatus(TransactionStatus.CANCELLED);

        return Optional.of(stockTransactionService.save(stockTransaction));
    }
}
