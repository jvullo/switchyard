package com.joevullo.switchyard.orders;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.joevullo.switchyard.orders.dto.OrderItem;
import com.joevullo.switchyard.orders.dto.PlaceOrderRequest;

@DisplayName("Business validation")
class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService(
                new ProductCatalogue(),
                new OrderProperties(new BigDecimal("5000.00"), "orders.events"));
    }

    @Test
    @DisplayName("accepts an order of known products and returns the computed total")
    void acceptsKnownProducts() {
        var request = requestWith(new OrderItem("PROD-CABLE", 4));

        var result = validationService.validate(request);

        assertThat(result)
                .isInstanceOfSatisfying(ValidationResult.Valid.class,
                        valid -> assertThat(valid.orderTotal()).isEqualByComparingTo("35.00"));
    }

    @Test
    @DisplayName("rejects a product the catalogue has never heard of")
    void rejectsUnknownProduct() {
        var request = requestWith(new OrderItem("PROD-NOPE", 1));

        var result = validationService.validate(request);

        assertThat(result)
                .isInstanceOfSatisfying(ValidationResult.Invalid.class,
                        invalid -> assertThat(invalid.reasons()).containsExactly("unknown product: PROD-NOPE"));
    }

    @Test
    @DisplayName("reports every unknown product at once rather than stopping at the first")
    void collectsAllFailures() {
        var request = requestWith(
                new OrderItem("PROD-NOPE", 1),
                new OrderItem("PROD-CABLE", 1),
                new OrderItem("PROD-ALSO-NOPE", 1));

        var result = validationService.validate(request);

        assertThat(result)
                .isInstanceOfSatisfying(ValidationResult.Invalid.class,
                        invalid -> assertThat(invalid.reasons())
                                .containsExactly("unknown product: PROD-NOPE", "unknown product: PROD-ALSO-NOPE"));
    }

    @Test
    @DisplayName("rejects an order above the configured maximum total")
    void rejectsOrderOverMaximum() {
        // 25 switches at 249.50 = 6237.50, over the 5000.00 configured above.
        var request = requestWith(new OrderItem("PROD-SWITCH", 25));

        var result = validationService.validate(request);

        assertThat(result)
                .isInstanceOfSatisfying(ValidationResult.Invalid.class,
                        invalid -> assertThat(invalid.reasons())
                                .singleElement().asString().contains("exceeds maximum"));
    }

    @Test
    @DisplayName("the maximum is configuration, not a constant")
    void maximumComesFromConfiguration() {
        var strict = new ValidationService(
                new ProductCatalogue(),
                new OrderProperties(new BigDecimal("10.00"), "orders.events"));

        var result = strict.validate(requestWith(new OrderItem("PROD-CABLE", 2))); // 17.50

        assertThat(result).isInstanceOf(ValidationResult.Invalid.class);
    }

    private static PlaceOrderRequest requestWith(OrderItem... items) {
        return new PlaceOrderRequest("CUST-1", List.of(items), "1 Sidings Way", "CARD");
    }
}
