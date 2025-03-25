package br.com.starter.application.api.garage;

import br.com.starter.application.api.common.GetPageRequest;
import br.com.starter.application.api.common.ResponseDTO;
import br.com.starter.application.api.garage.dtos.CreateGarageDTO;
import br.com.starter.application.api.garage.dtos.CreateGarageForExistingUsersDTO;
import br.com.starter.application.api.garage.dtos.UpdateGarageDTO;
import br.com.starter.application.useCase.garage.CreateGarageUseCase;
import br.com.starter.application.useCase.garage.GetPageGarageUseCase;
import br.com.starter.application.useCase.garage.GetPrimaryGarageUseCase;
import br.com.starter.application.useCase.garage.UpdateGarageUseCase;
import br.com.starter.application.useCase.garage.getGarageUseCase;
import br.com.starter.application.useCase.usersGarages.GetAllGaragesByUserUseCase;
import br.com.starter.domain.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/torque/api/garage")
@RequiredArgsConstructor
public class GarageController {

    private final CreateGarageUseCase createGarageUseCase;
    private final GetPageGarageUseCase getPageGarageUseCase;
    private final getGarageUseCase getGarageUseCase;
    private final UpdateGarageUseCase updateGarageUseCase;
    private final GetAllGaragesByUserUseCase getAllGaragesByUserUseCase;	
    private final GetPrimaryGarageUseCase getPrimaryGarageUseCase;

    @PostMapping
    public ResponseEntity<?> create(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @Valid @RequestBody CreateGarageDTO request
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                createGarageUseCase.handler(request)
            )
        );
    }

    @PostMapping("/create-for-existing-users")
    public ResponseEntity<?> createGarageForUser(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @Valid @RequestBody CreateGarageForExistingUsersDTO request
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                createGarageUseCase.handlerForExistingUsers(request)
            )
        );
    }

    @PutMapping("/{garageId}")
    public ResponseEntity<?> create(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @Valid @RequestBody UpdateGarageDTO request,
        @PathVariable UUID garageId
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                updateGarageUseCase.handler(garageId, request)
            )
        );
    }

    @PostMapping("/page/{page}")
    public ResponseEntity<?> page(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable Integer page,
        @RequestBody GetPageRequest request
    ){
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getPageGarageUseCase.handler(page, request)
            )
        );
    }

    @GetMapping("/{garageId}")
    public ResponseEntity<?> get(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable UUID garageId
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getGarageUseCase.handler(garageId)
            )
        );
    }

    @GetMapping("/get-all-garages/{userId}")
    public ResponseEntity<?> getAllGaragesByUser(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getAllGaragesByUserUseCase.handler(userId)
            )
        );
    }

    @GetMapping("/get-primary-garage/{userId}")
    public ResponseEntity<?> getPrimaryGarage(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getPrimaryGarageUseCase.handler(userId)
            )
        );
    }
}
