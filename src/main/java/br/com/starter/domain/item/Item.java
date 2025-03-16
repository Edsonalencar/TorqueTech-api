package br.com.starter.domain.item;

import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.vehicleType.VehicleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item {
    @Id
    private UUID id = UUID.randomUUID();

    private String name;
    private String description;
    private String code;

    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Enumerated(EnumType.STRING)
    private ItemStatus status = ItemStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "vehicle_type_id", foreignKey = @ForeignKey(name = "fk_vehicle_vehicle_type"))
    private VehicleType vehicleType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}