package br.com.starter.application.api.work.dtos;

import br.com.starter.domain.work.WorkStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
    public class GetPageWorkRequest {
    private Integer size = 10;
    private Set<String> licensePlate;
    private String query;
    private WorkStatus status;
    private LocalDateTime expectedAt;
    private UUID customerId;
    private UUID mechanicId;
}
