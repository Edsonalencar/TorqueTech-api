package br.com.starter.application.useCase.work;

import br.com.starter.application.api.stockTransaction.dtos.OutputStockTransactionRequest;
import br.com.starter.application.api.work.dtos.UpdateWorkDTO;
import br.com.starter.application.api.workOrder.dtos.UpdateWorkOrderDTO;
import br.com.starter.application.useCase.stockTransaction.CancelStockTransactionUseCase;
import br.com.starter.application.useCase.stockTransaction.OutputStockTransactionUseCase;
import br.com.starter.application.useCase.stockTransaction.UpdateOutStockTransactionUseCase;
import br.com.starter.domain.garage.Garage;
import br.com.starter.domain.garage.GarageService;
import br.com.starter.domain.stockTransaction.StockTransaction;
import br.com.starter.domain.stockTransaction.TransactionCategory;
import br.com.starter.domain.user.User;
import br.com.starter.domain.work.Work;
import br.com.starter.domain.work.WorkService;
import br.com.starter.domain.work.WorkStatus;
import br.com.starter.domain.workOrder.WorkOrder;
import br.com.starter.domain.workOrder.WorkOrderService;
import br.com.starter.domain.workOrder.WorkOrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateWorkUseCase {

    private final WorkService workService;
    private final GarageService garageService;
    private final WorkOrderService workOrderService;
    private final UpdateOutStockTransactionUseCase updateOutStockTransactionUseCase;
    private final OutputStockTransactionUseCase outputStockTransactionUseCase;
    private final CancelStockTransactionUseCase cancelStockTransactionUseCase;

    @Transactional
    public Optional<Work> handler(UUID workId, User owner, UpdateWorkDTO request) {

        Garage garage = garageService.getByUser(owner)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não possui uma oficina"
                ));

        Work work = workService.getByIdAndGarageId(workId, garage.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Usuário não autorizado!"
                ));


        work.setTitle(request.getTitle());
        work.setStartAt(request.getStartAt());
        work.setExpectedAt(request.getExpectedAt());
        work.setDescription(request.getDescription());
        work.setPrice(request.getPrice());
        work.setStatus(request.getStatus());
        work.setTotalCost(request.getTotalCost());

        if (request.getWorkOrders() != null) {
            for (UpdateWorkOrderDTO workOrderRequest : request.getWorkOrders()) {
                UUID workOrderId = workOrderRequest.getId();

                WorkOrder workOrder = workOrderService.getByIdAndWork(workOrderId, work.getId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "WorkOrder não encontrada para este Work!"
                        ));

                workOrder.setTitle(workOrderRequest.getTitle());
                workOrder.setDescription(workOrderRequest.getDescription());
                workOrder.setNote(workOrderRequest.getNote());
                workOrder.setStatus(workOrderRequest.getStatus());
                workOrder.setStartAt(workOrderRequest.getStartAt());
                workOrder.setExpectedAt(workOrderRequest.getExpectedAt());
                workOrder.setCost(workOrderRequest.getCost());
                workOrder.setPrice(workOrderRequest.getPrice());

                if (workOrderRequest.getStatus() == WorkOrderStatus.DELETED) {
                    if (workOrder.getStockTransaction() != null) {
                        cancelStockTransaction(workOrder.getStockTransaction().getId(), owner);
                    }
                } else if (workOrderRequest.getStockItems() != null && !workOrderRequest.getStockItems().isEmpty()) {
                    OutputStockTransactionRequest stockTransactionRequest = new OutputStockTransactionRequest();
                    stockTransactionRequest.setItems(workOrderRequest.getStockItems());
                    stockTransactionRequest.setTransactionAt(LocalDateTime.now());
                    stockTransactionRequest.setCategory(TransactionCategory.WORK_ORDER);

                    if (workOrder.getStockTransaction() != null) {
                        updateOutStockTransactionUseCase.handler(
                                owner,
                                workOrder.getStockTransaction().getId(),
                                stockTransactionRequest
                        );
                    } else {
                        Optional<?> stockTransactionOptional = outputStockTransactionUseCase.handler(owner, stockTransactionRequest);

                        if (stockTransactionOptional.isPresent()) {
                            StockTransaction stockTransaction = (StockTransaction) stockTransactionOptional.get();
                            workOrder.setStockTransaction(stockTransaction);
                        } else {
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Falha ao criar transação de estoque!");
                        }
                    }
                }

                workOrderService.save(workOrder);
            }
        }

        if (request.getStatus() == WorkStatus.COMPLETED) {
            work.setConcludedAt(LocalDateTime.now());
        } else if (request.getStatus() == WorkStatus.CANCELED) {
            work.setCancelledAt(LocalDateTime.now());

        }

        return Optional.of(workService.save(work));
    }

    private void cancelStockTransaction(UUID stockTransactionId, User owner) {
        cancelStockTransactionUseCase.handler(owner, stockTransactionId);
    }
}
