package br.com.starter.domain.workOrder;


import br.com.starter.domain.stockTransaction.StockTransaction;
import br.com.starter.domain.work.Work;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "work_order")
@Getter
@Setter
public class WorkOrder {
    @Id
    private UUID id = UUID.randomUUID();

    private String title;
    private String description;
    private String note;

    private Long cost;
    private Long price;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status = WorkOrderStatus.PENDING;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "stock_transaction_id", referencedColumnName = "id")
    private StockTransaction stockTransaction;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "work_id", nullable = false)
    private Work work;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime concludedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expectedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
