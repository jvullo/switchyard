package com.joevullo.switchyard.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.joevullo.switchyard.orders.dto.OrderItem;
import com.joevullo.switchyard.orders.dto.PlaceOrderRequest;

@Service
public class ValidationService {

    private final ProductCatalogue catalogue;
    private final OrderProperties properties;

    public ValidationService(ProductCatalogue catalogue, OrderProperties properties) {
        this.catalogue = catalogue;
        this.properties = properties;
    }

    public ValidationResult validate(PlaceOrderRequest request) {
        var reasons = new ArrayList<String>();

        for (OrderItem item : request.items()) {
            if (!catalogue.contains(item.productId())) {
                reasons.add("unknown product: " + item.productId());
            }
        }

        // Totalling needs every product priced, so only attempt it once they all resolved.
        if (reasons.isEmpty()) {
            BigDecimal total = totalFor(request.items());

            if (total.compareTo(properties.maxOrderTotal()) > 0) {
                reasons.add("order total %s exceeds maximum of %s"
                        .formatted(total, properties.maxOrderTotal()));
            } else {
                return new ValidationResult.Valid(total);
            }
        }

        return new ValidationResult.Invalid(reasons);
    }

    private BigDecimal totalFor(List<OrderItem> items) {
        return items.stream()
                .map(item -> catalogue.priceOf(item.productId())
                        .orElseThrow()
                        .multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
