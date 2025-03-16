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

@Component
@RequiredArgsConstructor
public class CreateItemUseCase {
    private final ItemService itemService;
    private final GarageService garageService;
    private final VehicleTypeService vehicleTypeService;

    public Optional<?> handler(User user, CreateItemRequest request) {
        var garage = garageService.getByUser(user).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada"
            )
        );

        ModelMapper mapper = new ModelMapper();

        var item = mapper.map(request, Item.class);
        item.setGarage(garage);

        if (request.getVehicleTypeId() != null) {
            var vehicleType = vehicleTypeService.findById(request.getVehicleTypeId())
                .orElseThrow(() ->
                    new IllegalArgumentException("Tipo de veículo não encontrado")
                );
            item.setVehicleType(vehicleType);
        }

        return Optional.of(itemService.save(item));
    }
}
