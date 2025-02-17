package br.com.starter.domain.vehicleType;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "VehicleType")
@Getter
@Setter
public class VehicleType {
    @Id
    private UUID id = UUID.randomUUID();

    private String model;
    private String brand;
    private String year;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
