package br.com.starter.application.useCase.customer;

import br.com.starter.application.api.common.GetPageRequest;
import br.com.starter.domain.customer.CustomerService;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetPageCustomerUseCase {

    private final GarageService garageService;
    private final CustomerService customerService;

    public Optional<?> handler(User user, Integer page, GetPageRequest request) {
        Garage garage = garageService.getByUser(user).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada!"
            )
        );

        return Optional.of(customerService.getPage(
            garage,
            request.getQuery(),
            request.getStatus(),
            PageRequest.of(page, request.getSize())
        ));
    }
}
