package br.com.starter.application.api.stockTransaction.dtos;

import br.com.starter.domain.stockTransaction.TransactionCategory;
import br.com.starter.domain.stockTransaction.TransactionType;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class GetPageStockTransactionRequest {
    private Integer size = 10;
    private Set<UUID> itemsIs;
    private Set<UUID> ids;
    private UUID ownerId;
    private TransactionType transactionType;
    private TransactionCategory category;
    private String query = "";
}