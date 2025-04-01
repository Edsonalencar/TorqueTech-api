package br.com.starter.application.useCase.garage;

import br.com.starter.application.api.garage.dtos.UpdateGarageDTO;
import br.com.starter.application.api.user.dto.UpdateUserDTO;
import br.com.starter.domain.address.Address;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateGarageUseCase {
    private final GarageService garageService;
    private final UserService userService;

    @Transactional
    public Optional<Garage> handler(UUID garageId, UpdateGarageDTO request) {

        var garage = garageService.getById(garageId).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Garagem não encontrada!"
            )
        );

        garage.setName(request.getName());
        garage.setCnpj(request.getCnpj());

        ModelMapper mapper = new ModelMapper();

        // Clona informações de endereço
        if (request.getAddress() != null) {
            var address = mapper.map(request.getAddress(), Address.class);
            garage.setAddress(address);
        } else garage.setAddress(null);

        // Clona informações de criação de usuário
        var updateRequest = mapper.map(request, UpdateUserDTO.class);
        updateRequest.setName(request.getOwnerName()); // seta o nome certo pois a clonagem vai pegar o getName que é o nome da oficina


        var user = userService.update(request.getOwnerId(), updateRequest);
        garage.setOwner(user);

        return Optional.of(garageService.save(garage));
    }
}
