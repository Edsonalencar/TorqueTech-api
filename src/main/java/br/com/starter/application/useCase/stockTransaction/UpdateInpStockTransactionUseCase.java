package br.com.starter.application.useCase.stockTransaction;

import br.com.starter.application.api.stockTransaction.dtos.InputStockItemDTO;
import br.com.starter.application.api.stockTransaction.dtos.InputStockTransactionRequest;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateInpStockTransactionUseCase {
    private final StockTransactionService stockTransactionService;
    private final StockItemService stockItemService;
    private final GarageService garageService;
    private final LocalService localService;
    private final ItemService itemService;

    @Transactional
    public Optional<?> handler(
        User user,
        UUID stockTransactionId,
        InputStockTransactionRequest request
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

        stockTransaction = updateStockTransaction(stockTransaction, request);

        var transactionItems = new ArrayList<TransactionItem>();
        StockTransaction finalStockTransaction = stockTransaction;

        request.getItems().forEach(item -> {
            var transactionItem = updateStockItem(finalStockTransaction, item);
            transactionItems.add(transactionItem);
        });

        finalStockTransaction.getItems().clear();
        finalStockTransaction.setItems(transactionItems);

        return Optional.of(stockTransactionService.save(finalStockTransaction));
    }

    public StockTransaction updateStockTransaction(
         StockTransaction stockTransaction,
        InputStockTransactionRequest request
    ) {
        stockTransaction.setCategory(request.getCategory());
        stockTransaction.setTransactionDate(request.getTransactionAt());

        var transActionTotalQuantity = request.getItems().stream()
            .map(InputStockItemDTO::getQuantity)
            .reduce(0, Integer::sum);

        var transActionTotalItemsPrice = request.getItems().stream()
            .map(InputStockItemDTO::getPrice)
            .reduce(0L, Long::sum);

        stockTransaction.setQuantity(transActionTotalQuantity);
        stockTransaction.setPrice(transActionTotalItemsPrice * transActionTotalQuantity);

        return stockTransaction;
    }

    private TransactionItem updateStockItem (
        StockTransaction stockTransaction,
        InputStockItemDTO itemRequest
    ) {
        var transActionItem = new TransactionItem();
        transActionItem.setTransaction(stockTransaction);
        transActionItem.setQuantity(itemRequest.getQuantity());
        transActionItem.setPrice(itemRequest.getPrice());
        transActionItem.setDiscount(itemRequest.getDiscount());

        StockItem stockItem = null;

        if(itemRequest.getStockItemId() != null){
            stockItem = stockItemService.findById(
                itemRequest.getStockItemId(),
                stockTransaction.getGarage()
            ).orElse(null);

            if(stockItem != null) {
                StockItem finalStockItem = stockItem;
                var transActionitem =stockTransaction.getItems().stream()
                    .filter(item -> item.getStockItem().getId() == finalStockItem.getId())
                    .findFirst().orElseThrow();

                stockItem.setQuantity(stockItem.getQuantity() - transActionitem.getQuantity());
            }
        }

        if (stockItem == null) {
            stockItem = new StockItem();
            stockItem.setGarage(stockTransaction.getGarage());
        }

        var item = itemService.getById(itemRequest.getItemId(), stockTransaction.getGarage()).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Item não encontrado!"
            )
        );

        var local = localService.getByIdAndGarageId(itemRequest.getLocalId(), stockTransaction.getGarage().getId()).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Item não encontrado!"
            )
        );

        stockItem.setItem(item);
        stockItem.setLocal(local);
        stockItem.setAcquisitionPrice(itemRequest.getAcquisitionUnitPrice());
        stockItem.setPrice(itemRequest.getPrice());
        stockItem.setAcquisitionAt(stockTransaction.getTransactionDate().toLocalDate());

        stockItem.setQuantity(stockItem.getQuantity() + itemRequest.getQuantity());

        stockItem = stockItemService.save(stockItem);

        transActionItem.setStockItem(stockItem);


        return transActionItem;
    }
}
