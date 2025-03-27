package br.com.starter.application.api.mechanic;

import br.com.starter.application.api.common.GetPageRequest;
import br.com.starter.application.api.common.ResponseDTO;
import br.com.starter.application.api.mechanic.dtos.CreateMechanicDTO;
import br.com.starter.application.api.mechanic.dtos.UpdateMechanicDTO;
import br.com.starter.application.useCase.mechanic.*;
import br.com.starter.domain.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/torque/api/mechanic")
@RequiredArgsConstructor
public class MechanicController {
    private final CreateMechanicUseCase createMechanicUseCase;
    private final GetPageMechanicUseCase getPageMechanicUseCase;
    private final GetMechanicUseCase getMechanicUseCase;
    private final UpdateMechanicUseCase updateMechanicUseCase;
    private final GetAllMechanicsUseCase getAllMechanicsUseCase;
    private final ChangePrimaryGarageMechanicUseCase changePrimaryGarageUseCase;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @Valid @RequestBody CreateMechanicDTO request
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        createMechanicUseCase.handler(request, user)
                )
        );
    }

    @PutMapping("/{mechanicId}")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @Valid @RequestBody UpdateMechanicDTO request,
            @PathVariable UUID mechanicId
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        updateMechanicUseCase.handler(mechanicId, request, user)
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
                        getPageMechanicUseCase.handler(page, request, userAuthentication.getUser())
                )
        );
    }

    @GetMapping("/{mechanicId}")
    public ResponseEntity<?> get(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @PathVariable UUID mechanicId
    ) {
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        getMechanicUseCase.handler(mechanicId)
                )
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(
        @AuthenticationPrincipal CustomUserDetails userAuthentication
    ) {
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getAllMechanicsUseCase.handler(userAuthentication.getUser())
            )
        );
    }

}
