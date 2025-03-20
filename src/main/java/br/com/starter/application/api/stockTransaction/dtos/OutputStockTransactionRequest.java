package br.com.starter.application.api.stockTransaction.dtos;

import br.com.starter.domain.stockTransaction.TransactionCategory;
import br.com.starter.domain.stockTransaction.TransactionType;
import br.com.starter.domain.workOrder.WorkOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OutputStockTransactionRequest {
    private List<OutputStockItemDTO> items;
    private LocalDateTime transactionAt;
    private TransactionCategory category;
    private TransactionType type = TransactionType.OUTPUT;
    private WorkOrder workOrder = null;
}
