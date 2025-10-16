package com.joevullo.switchyard.orders;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class ProductCatalogue {

    private static final Map<String, BigDecimal> PRICES = Map.of(
            "PROD-ROUTER", new BigDecimal("129.99"),
            "PROD-SWITCH", new BigDecimal("249.50"),
            "PROD-CABLE", new BigDecimal("8.75"));

    public boolean contains(String productId) {
        return PRICES.containsKey(productId);
    }

    public Optional<BigDecimal> priceOf(String productId) {
        return Optional.ofNullable(PRICES.get(productId));
    }
}
