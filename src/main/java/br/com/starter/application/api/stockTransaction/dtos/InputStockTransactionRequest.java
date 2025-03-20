package br.com.starter.application.api.stockTransaction.dtos;

import br.com.starter.domain.stockTransaction.TransactionCategory;
import br.com.starter.domain.stockTransaction.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InputStockTransactionRequest {
    private List<InputStockItemDTO> items;
    private TransactionCategory category;
    private LocalDateTime transactionAt;
}
