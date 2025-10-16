package com.joevullo.switchyard.orders;

import java.math.BigDecimal;
import java.util.List;

public sealed interface ValidationResult {

    /** The request is acceptable. Carries the order total computed during validation. */
    record Valid(BigDecimal orderTotal) implements ValidationResult {
    }

    /** The request broke one or more business rules. Never empty. */
    record Invalid(List<String> reasons) implements ValidationResult {

        public Invalid {
            if (reasons == null || reasons.isEmpty()) {
                throw new IllegalArgumentException("an Invalid result must carry at least one reason");
            }
            reasons = List.copyOf(reasons);
        }
    }
}
