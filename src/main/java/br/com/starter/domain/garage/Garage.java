package br.com.starter.domain.garage;

import br.com.starter.domain.address.Address;
import br.com.starter.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "garage")
@Getter
@Setter
public class Garage {
    @Id
    private UUID id = UUID.randomUUID();
    private String name;
    private String cnpj;

    @ManyToOne
    private User owner;
    @OneToOne
    private Address address;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();
}
