package br.com.starter.application.useCase.item;

import br.com.starter.application.api.item.dtos.CreateItemRequest;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.item.Item;
import br.com.starter.domain.item.ItemService;
import br.com.starter.domain.user.User;
import br.com.starter.domain.vehicleType.VehicleTypeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateItemUseCase {
    private final ItemService itemService;
    private final GarageService garageService;
    private final VehicleTypeService vehicleTypeService;

    public Optional<?> handler(
        User user,
        UUID itemId,
        CreateItemRequest request
    ) {
        var garage = garageService.getByUser(user).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada"
            )
        );

        var item = itemService.getById(itemId, garage).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Item não encontrado!"
            )
        );

        if (request.getVehicleTypeId() != null) {
            var vehicleType = vehicleTypeService.findById(request.getVehicleTypeId())
                .orElseThrow(() ->
                    new IllegalArgumentException("Tipo de veículo não encontrado")
                );

            item.setVehicleType(vehicleType);
        }

        if (request.getCode() != null)
            item.setCode(request.getCode());

        item.setCategory(request.getCategory());
        item.setName(request.getName());
        item.setDescription(request.getDescription());

        return Optional.of(itemService.save(item));
    }
}
