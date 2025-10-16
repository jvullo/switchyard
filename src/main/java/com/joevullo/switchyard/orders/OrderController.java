package com.joevullo.switchyard.orders;

import com.joevullo.switchyard.orders.dto.PlaceOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {

    private final ValidationService validationService;

    public OrderController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/order")
    public ResponseEntity<?> handlePost(@Valid @RequestBody PlaceOrderRequest placeOrderRequest) {
        return extracted(placeOrderRequest); 
    }

    private ResponseEntity<?> extracted(PlaceOrderRequest placeOrderRequest) {
        return switch (validationService.validate(placeOrderRequest)) {

            case ValidationResult.Valid(BigDecimal total) -> {
                yield ResponseEntity.accepted().body(new OrderAccepted(UUID.randomUUID().toString(), total));
            }

            case ValidationResult.Invalid(List<String> reasons) ->
                ResponseEntity.unprocessableEntity().body(new OrderRejected(reasons));
        };
    }

    public record OrderAccepted(String orderId, BigDecimal orderTotal) {}

    public record OrderRejected(List<String> reasons) {}
}
