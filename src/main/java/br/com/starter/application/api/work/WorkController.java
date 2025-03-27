package br.com.starter.application.api.work;

import br.com.starter.application.api.common.ResponseDTO;
import br.com.starter.application.api.work.dtos.*;
import br.com.starter.application.useCase.work.*;
import br.com.starter.domain.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/torque/api/work")
@RequiredArgsConstructor
public class WorkController {

    private final CreateWorkRequestUseCase createWorkRequestUseCase;
    private final GetPageWorkUseCase getPageWorkUseCase;
    private final UpdateWorkUseCase updateWorkUseCase;
    private final UpdateWorkStatusUseCase updateWorkStatusUseCase;
    private final GetWorkUseCase getWorkUseCase;

    @PostMapping
    public ResponseEntity<?> create(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @RequestBody CreateWorkRequestDTO request
    ) {
        var owner = userAuthentication.getUser();
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        createWorkRequestUseCase.handler(request, owner)
                )
        );
    }

    @PostMapping("/page/{page}")
    public ResponseEntity<?> search(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @RequestBody GetPageWorkRequest request,
            @PathVariable Integer page
    ) {
        var owner = userAuthentication.getUser();
        Page<?> worksPage = getPageWorkUseCase.handler(owner, request, page);

        return ResponseEntity.ok(new ResponseDTO<>(worksPage));
    }

    @GetMapping("/{workId}")
    public ResponseEntity<?> getById(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable UUID workId
    ) {
        var owner = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getWorkUseCase.handler(owner, workId)
            )
        );
    }

    @PutMapping("/{workId}")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal CustomUserDetails userAuthentication,
            @Valid @RequestBody UpdateWorkDTO request,
            @PathVariable UUID workId
    ) {
        var owner = userAuthentication.getUser();
        return ResponseEntity.ok(
                new ResponseDTO<>(
                        updateWorkUseCase.handler(workId, owner, request)
                )
        );
    }

    @PutMapping("/{workId}/status")
    public ResponseEntity<?> updateStatus(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @Valid @RequestBody UpdateWorkStatusRequest request,
        @PathVariable UUID workId
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                updateWorkStatusUseCase.handler(user, workId, request)
            )
        );
    }
}
