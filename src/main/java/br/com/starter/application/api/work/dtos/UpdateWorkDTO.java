package br.com.starter.application.api.work.dtos;

import br.com.starter.application.api.workOrder.dtos.UpdateWorkOrderDTO;
import br.com.starter.domain.work.WorkStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UpdateWorkDTO {
    private String title;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime expectedAt;
    private WorkStatus status;
    private Long price;
    private Long totalCost;
    private List<UpdateWorkOrderDTO> workOrders;
}
