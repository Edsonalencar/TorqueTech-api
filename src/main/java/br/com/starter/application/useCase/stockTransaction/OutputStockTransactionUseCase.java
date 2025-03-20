package br.com.starter.application.useCase.stockTransaction;

import br.com.starter.application.api.stockTransaction.dtos.InputStockItemDTO;
import br.com.starter.application.api.stockTransaction.dtos.InputStockTransactionRequest;
import br.com.starter.application.api.stockTransaction.dtos.OutputStockItemDTO;
import br.com.starter.application.api.stockTransaction.dtos.OutputStockTransactionRequest;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.item.ItemService;
import br.com.starter.domain.local.LocalService;
import br.com.starter.domain.stockItem.StockItem;
import br.com.starter.domain.stockItem.StockItemService;
import br.com.starter.domain.stockTransaction.StockTransaction;
import br.com.starter.domain.stockTransaction.StockTransactionService;
import br.com.starter.domain.stockTransaction.TransactionType;
import br.com.starter.domain.transactionItem.TransactionItem;
import br.com.starter.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OutputStockTransactionUseCase {
    private final StockTransactionService stockTransactionService;
    private final StockItemService stockItemService;
    private final GarageService garageService;

    public Optional<StockTransaction> handler(
        User user,
        OutputStockTransactionRequest request
    ) {
        Garage garage = garageService.getByUser(user).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada!"
            )
        );

        if (request.getType() != TransactionType.OUTPUT) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Somente saída e orçamento!"
            );
        }

        var stockTransaction = new StockTransaction();
        stockTransaction.setType(request.getType());
        stockTransaction.setGarage(garage);
        stockTransaction.setCategory(request.getCategory());
        stockTransaction.setOwner(user);
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

        List<TransactionItem> transactionItems = new ArrayList<>();

        request.getItems().forEach(item -> {
            var transactionitem = getTransactionitem(stockTransaction, item);
            transactionItems.add(transactionitem);
        });

        if(stockTransaction.getItems() == null)
            stockTransaction.setItems(new ArrayList<>());

        stockTransaction.setItems(transactionItems);

        return Optional.of(stockTransactionService.save(stockTransaction));
    }

    public TransactionItem getTransactionitem (
        StockTransaction transaction,
        OutputStockItemDTO itemRequest
    ) {
        var transActionItem = new TransactionItem();
        transActionItem.setQuantity(itemRequest.getQuantity());
        transActionItem.setTransaction(transaction);
        transActionItem.setPrice(itemRequest.getPrice());
        transActionItem.setDiscount(itemRequest.getDiscount());

        var stockItem = stockItemService.findById(
            itemRequest.getStockItemId(),
            transaction.getGarage()
        ).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Item não encontrado!"
            )
        );

        if(transaction.getType() == TransactionType.OUTPUT) {
            if(stockItem.getQuantity() < itemRequest.getQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O estoque não possui esta quantidade de items!"
                );
            }

            stockItem.setQuantity(stockItem.getQuantity() - itemRequest.getQuantity());
        }
        
        stockItem = stockItemService.save(stockItem);

        transActionItem.setStockItem(stockItem);

        return transActionItem;
    }
}
