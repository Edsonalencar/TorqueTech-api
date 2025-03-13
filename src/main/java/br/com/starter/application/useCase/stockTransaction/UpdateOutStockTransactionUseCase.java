package br.com.starter.application.useCase.stockTransaction;

import br.com.starter.application.api.stockTransaction.dtos.InputStockItemDTO;
import br.com.starter.application.api.stockTransaction.dtos.OutputStockItemDTO;
import br.com.starter.application.api.stockTransaction.dtos.OutputStockTransactionRequest;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.stockItem.StockItem;
import br.com.starter.domain.stockItem.StockItemService;
import br.com.starter.domain.stockTransaction.StockTransaction;
import br.com.starter.domain.stockTransaction.StockTransactionService;
import br.com.starter.domain.stockTransaction.TransactionStatus;
import br.com.starter.domain.transactionItem.TransactionItem;
import br.com.starter.domain.transactionItem.TransactionItemService;
import br.com.starter.domain.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateOutStockTransactionUseCase {
    private final StockTransactionService stockTransactionService;
    private final TransactionItemService transactionItemService;
    private final StockItemService stockItemService;
    private final GarageService garageService;

    @Transactional
    public Optional<?> handler(
        User user,
        UUID stockTransactionId,
        OutputStockTransactionRequest request
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

        stockTransaction.setCategory(request.getCategory());
        stockTransaction.setTransactionDate(request.getTransactionAt());

        if (request.getWorkOrder() != null)
            stockTransaction.setWorkOrder(request.getWorkOrder());

        var transActionTotalQuantity = request.getItems().stream()
                .map(OutputStockItemDTO::getQuantity)
                .reduce(0, Integer::sum);

        var transActionTotalItemsPrice = request.getItems().stream()
                .map(OutputStockItemDTO::getPrice)
                .reduce(0L, Long::sum);

        stockTransaction.setQuantity(transActionTotalQuantity);
        stockTransaction.setPrice(transActionTotalItemsPrice * transActionTotalQuantity);
        stockTransaction.setTransactionDate(request.getTransactionAt());

        List<TransactionItem> transactionItems = new ArrayList<>();

        request.getItems().forEach(item -> {
            var transactionitem = updateTransactionitem(stockTransaction, item);
            transactionItems.add(transactionitem);
        });

        transactionItemService.deleteAll(stockTransaction.getItems());
        stockTransaction.getItems().clear();
        stockTransaction.setItems(transactionItems);

        return Optional.of(stockTransactionService.save(stockTransaction));
    }

    private TransactionItem updateTransactionitem(
        StockTransaction stockTransaction,
        OutputStockItemDTO itemRequest
    ) {
        var transActionItem = new TransactionItem();
        transActionItem.setTransaction(stockTransaction);
        transActionItem.setQuantity(itemRequest.getQuantity());

        var stockItem = stockItemService.findById(
            itemRequest.getStockItemId(),
            stockTransaction.getGarage()
        ).orElseThrow();

        StockItem finalStockItem = stockItem;
        var transActionitem =stockTransaction.getItems().stream()
            .filter(item -> item.getStockItem().getId() == finalStockItem.getId())
            .findFirst().orElseThrow();

        stockItem.setQuantity(stockItem.getQuantity() + transActionitem.getQuantity());

        if(stockItem.getQuantity() < itemRequest.getQuantity()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O estoque não possui esta quantidade de items!"
            );
        }

        stockItem.setQuantity(stockItem.getQuantity() - itemRequest.getQuantity());
        stockItem = stockItemService.save(stockItem);

        transActionItem.setStockItem(stockItem);

        return transActionItem;
    }
}
