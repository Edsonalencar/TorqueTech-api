package br.com.starter.application.api.stockItem;

import br.com.starter.application.api.common.ResponseDTO;
import br.com.starter.application.api.stockItem.dto.GetPageStockItemRequest;
import br.com.starter.application.api.stockTransaction.dtos.GetPageStockTransactionRequest;
import br.com.starter.application.useCase.stockItem.GetAllStockItemUseCase;
import br.com.starter.application.useCase.stockItem.GetPageStockItemUseCase;
import br.com.starter.domain.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/torque/api/stock-item")
@RequiredArgsConstructor
public class StockItemController {
    private final GetAllStockItemUseCase getAllStockItemUseCase;
    private final GetPageStockItemUseCase getPageStockItemUseCase;

    @GetMapping
    public ResponseEntity<?> page(
        @AuthenticationPrincipal CustomUserDetails userAuthentication
    ){
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getAllStockItemUseCase.handler(user)
            )
        );
    }

    @PostMapping("/page/{page}")
    public ResponseEntity<?> page(
        @AuthenticationPrincipal CustomUserDetails userAuthentication,
        @PathVariable Integer page,
        @Valid @RequestBody GetPageStockItemRequest request
    ) {
        var user = userAuthentication.getUser();
        return ResponseEntity.ok(
            new ResponseDTO<>(
                getPageStockItemUseCase.handler(
                    user,
                    request,
                    page
                )
            )
        );
    }
}
