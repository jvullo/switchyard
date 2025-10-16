package com.joevullo.switchyard.orders.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

/**
 * The inbound API payload for placing an order.
 *
 * <p>Note the {@code @Valid} on {@code items}: without it Bean Validation checks that the
 * list is non-empty but does <em>not</em> descend into the elements, so a blank productId
 * would sail straight through. Cascading into collection elements is always opt-in.
 */
public record PlaceOrderRequest(

        @NotBlank(message = "customerId must not be blank")
        String customerId,

        @NotEmpty(message = "an order must contain at least one item")
        @Valid
        List<OrderItem> items,

        @NotBlank(message = "shippingAddress must not be blank")
        String shippingAddress,

        @NotBlank(message = "paymentMethod must not be blank")
        String paymentMethod) {
}
