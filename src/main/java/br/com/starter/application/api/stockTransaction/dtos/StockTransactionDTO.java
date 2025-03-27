package br.com.starter.application.api.stockTransaction.dtos;

import br.com.starter.application.api.workOrder.dtos.WorkOrderDTO;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.stockTransaction.StockTransaction;
import br.com.starter.domain.stockTransaction.TransactionCategory;
import br.com.starter.domain.stockTransaction.TransactionStatus;
import br.com.starter.domain.stockTransaction.TransactionType;
import br.com.starter.domain.transactionItem.TransactionItem;
import br.com.starter.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class StockTransactionDTO {
    private UUID id;
    private Long price;
    private Integer quantity;
    private TransactionCategory category;
    private TransactionStatus status;
    private TransactionType type;
    private Garage garage;
    private User owner;
    private WorkOrderDTO workOrder;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private List<TransactionItem> items;

    public StockTransactionDTO(StockTransaction transaction) {
        this.id = transaction.getId();
        this.price = transaction.getPrice();
        this.quantity = transaction.getQuantity();
        this.category = transaction.getCategory();
        this.status = transaction.getStatus();
        this.type = transaction.getType();
        this.garage = transaction.getGarage();
        this.owner = transaction.getOwner();
        this.transactionDate = transaction.getTransactionDate();
        this.createdAt = transaction.getCreatedAt();
        this.items = transaction.getItems();

        if (transaction.getWorkOrder() != null)
            this.workOrder = new WorkOrderDTO(transaction.getWorkOrder());
    }
}
