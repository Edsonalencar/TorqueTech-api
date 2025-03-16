package br.com.starter.application.api.item.dtos;

import br.com.starter.domain.item.ItemCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateItemRequest {
    private String name;
    private String description;
    private ItemCategory category;
    private String code;
    private UUID vehicleTypeId;
}
