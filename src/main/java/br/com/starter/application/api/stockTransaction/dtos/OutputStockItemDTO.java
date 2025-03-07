package br.com.starter.application.api.stockTransaction.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class OutputStockItemDTO {
    private UUID stockItemId;
    private Long price;
    private Integer quantity;
}
