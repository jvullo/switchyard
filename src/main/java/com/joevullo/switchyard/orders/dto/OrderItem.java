package com.joevullo.switchyard.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * A single line on an order.
 *
 * <p>Annotations here are <em>structural</em> validation only: they answer "is this
 * request well-formed?" without needing to look anything up. Whether the product actually
 * exists is a business question and lives in ValidationService.
 */
public record OrderItem(

        @NotBlank(message = "productId must not be blank")
        String productId,

        @Positive(message = "quantity must be greater than zero")
        int quantity) {
}
