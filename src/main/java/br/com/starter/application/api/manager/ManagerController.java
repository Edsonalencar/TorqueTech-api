package br.com.starter.application.api.manager;

import br.com.starter.application.api.common.GetPageRequest;
import br.com.starter.application.api.common.ResponseDTO;
import br.com.starter.application.api.manager.dtos.CreateManagerDTO;
import br.com.starter.application.api.manager.dtos.UpdateManagerDTO;
import br.com.starter.application.useCase.manager.ChangePrimaryGarageManagerUseCase;
import br.com.starter.application.useCase.manager.CreateManagerUseCase;
import br.com.starter.application.useCase.manager.GetManagerUseCase;
import br.com.starter.application.useCase.manager.GetPageManagerUseCase;
import br.com.starter.application.useCase.manager.UpdateManagerUseCase;
import br.com.starter.domain.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/torque/api/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final CreateManagerUseCase createManagerUseCase;
    private final GetPageManagerUseCase getPageManagerUseCase;
    private final GetManagerUseCase getManagerUseCase;
    private final UpdateManagerUseCase updateManagerUseCase;
    private final ChangePrimaryGarageManagerUseCase changePrimaryGarageUseCase;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @Valid @RequestBody CreateManagerDTO request
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        createManagerUseCase.handler(request, user)
                )
        );
    }

    @PutMapping("/{managerId}")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @Valid @RequestBody UpdateManagerDTO request,
            @PathVariable UUID managerId
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        updateManagerUseCase.handler(managerId, request, user)
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
                        getPageManagerUseCase.handler(page, request, userAuthentication.getUser())
                )
        );
    }

    @GetMapping("/{managerId}")
    public ResponseEntity<?> get(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @PathVariable UUID managerId
    ) {
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        getManagerUseCase.handler(managerId)
                )
        );
    }
}