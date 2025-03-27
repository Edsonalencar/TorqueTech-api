package br.com.starter.application.api.customer;

import br.com.starter.application.api.common.GetPageRequest;
import br.com.starter.application.api.common.ResponseDTO;
import br.com.starter.application.api.customer.dtos.CreateCustomerDTO;
import br.com.starter.application.api.customer.dtos.UpdateCustomerDTO;
import br.com.starter.application.useCase.customer.*;

import br.com.starter.domain.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/torque/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final GetPageCustomerUseCase getPageCustomerUseCase;
    private final GetAllCustomerUseCase getAllCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;

    @PostMapping
    public ResponseEntity<?> create(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @Valid @RequestBody CreateCustomerDTO createCustomerDTO
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                createCustomerUseCase.handler(createCustomerDTO, userAuthentication.getUser())
            )
        );
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<?> update(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @Valid @RequestBody UpdateCustomerDTO request,
        @PathVariable UUID customerId
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                updateCustomerUseCase.handler(user, customerId, request)
            )
        );
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> get(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable UUID customerId
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getCustomerUseCase.handler(user, customerId)
            )
        );
    }

    @GetMapping
    public ResponseEntity<?> get(
        @AuthenticationPrincipal CustomUserDetails userAuthentication
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getAllCustomerUseCase.handler(user)
            )
        );
    }

    @PostMapping("/page/{page}")
    public ResponseEntity<?> page(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable Integer page,
        @RequestBody GetPageRequest request
    ){
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getPageCustomerUseCase.handler(user, page, request)
            )
        );
    }
}
