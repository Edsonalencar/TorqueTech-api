package br.com.starter.domain.stockTransaction;

import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.stockItem.StockItem;
import br.com.starter.domain.transactionItem.TransactionItem;
import br.com.starter.domain.user.User;
import br.com.starter.domain.workOrder.WorkOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stock_transaction")
@Getter
@Setter
public class StockTransaction {
    @Id
    private UUID id = UUID.randomUUID();

    private Long price;
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private TransactionCategory category;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.CREATED;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TransactionItem> items = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
