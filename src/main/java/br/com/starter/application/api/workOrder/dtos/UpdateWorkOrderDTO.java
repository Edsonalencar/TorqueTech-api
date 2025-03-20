package br.com.starter.application.api.workOrder.dtos;

import br.com.starter.application.api.stockTransaction.dtos.OutputStockItemDTO;
import br.com.starter.domain.workOrder.WorkOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateWorkOrderDTO {
    private UUID id;
    private String title;
    private String description;
    private String note;
    private WorkOrderStatus status;
    private LocalDateTime startAt;
    private LocalDateTime expectedAt;
    private Long cost;
    private Long price;
    private List<OutputStockItemDTO> stockItems;
}
