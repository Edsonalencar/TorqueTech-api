package br.com.starter.application.api.vehicle;

import br.com.starter.application.api.common.GetPageRequest;
import br.com.starter.application.api.common.ResponseDTO;
import br.com.starter.application.api.vehicle.dtos.CreateVehicleDTO;
import br.com.starter.application.api.vehicle.dtos.UpdateVehicleDTO;
import br.com.starter.application.useCase.vehicle.*;
import br.com.starter.domain.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/torque/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetPageVehicleUseCase getPageVehicleUseCase;
    private final GetVehicleUseCase getVehicleUseCase;
    private final GetPageVehicleByCustomerUseCase getPageVehicleByCustomerUseCase;
    private final UpdateVehicleUseCase updateVehicleUseCase;
    private final GetAllVehiclesByCustomerUseCase getAllVehiclesUseCase;


    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @Valid @RequestBody CreateVehicleDTO request
    ) {
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        createVehicleUseCase.handler(request, userAuthentication.getUser())
                )
        );
    }


    @PutMapping("/{vehicleId}")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @Valid @RequestBody UpdateVehicleDTO request,
            @PathVariable UUID vehicleId
    ) {
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        updateVehicleUseCase.handler(vehicleId, request)
                )
        );
    }

    @PostMapping("/page/{page}")
    public ResponseEntity<?> page(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @PathVariable Integer page,
            @RequestBody GetPageRequest request
    ) {
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        getPageVehicleUseCase.handler(page, request, userAuthentication.getUser())
                )
        );
    }

    @PostMapping("/customer/{customerId}/page/{page}")
    public ResponseEntity<?> page(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @PathVariable UUID customerId,
            @PathVariable Integer page,
            @RequestBody GetPageRequest request
    ) {
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        getPageVehicleByCustomerUseCase.handler(customerId, page, request, userAuthentication.getUser())
                )
        );
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<?> get(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @PathVariable UUID vehicleId
    ) {
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        getVehicleUseCase.handler(vehicleId)
                )
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAllByCustomer(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable UUID customerId
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getAllVehiclesUseCase.handler(customerId)
            )
        );
    }
}
