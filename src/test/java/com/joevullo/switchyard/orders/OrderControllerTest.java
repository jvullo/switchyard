package com.joevullo.switchyard.orders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joevullo.switchyard.orders.dto.OrderItem;
import com.joevullo.switchyard.orders.dto.PlaceOrderRequest;

@WebMvcTest(OrderController.class)
@DisplayName("POST /order")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ValidationService validationService;

    @Test
    @DisplayName("returns 202 with an order id when validation passes")
    void acceptsValidOrder() throws Exception {
        given(validationService.validate(any()))
                .willReturn(new ValidationResult.Valid(new BigDecimal("35.00")));

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(validRequest())))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.orderTotal").value(35.00));
    }

    @Test
    @DisplayName("returns 422 with reasons when business validation fails")
    void rejectsBusinessFailure() throws Exception {
        given(validationService.validate(any()))
                .willReturn(new ValidationResult.Invalid(List.of("unknown product: PROD-NOPE")));

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(validRequest())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.reasons[0]").value("unknown product: PROD-NOPE"));
    }

    // The important one: structural failures are rejected by @Valid before the
    // controller body runs, so business validation is never consulted.
    @Test
    @DisplayName("returns 400 for a blank customerId without reaching business validation")
    void rejectsBlankCustomerId() throws Exception {
        var request = new PlaceOrderRequest("  ", List.of(new OrderItem("PROD-CABLE", 1)), "1 Sidings Way", "CARD");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isBadRequest());

        verify(validationService, never()).validate(any());
    }

    @Test
    @DisplayName("returns 400 for an empty items list")
    void rejectsEmptyItems() throws Exception {
        var request = new PlaceOrderRequest("CUST-1", List.of(), "1 Sidings Way", "CARD");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isBadRequest());

        verify(validationService, never()).validate(any());
    }

    // Proves the @Valid on the items list actually cascades. Without it this returns 202.
    @Test
    @DisplayName("returns 400 for a non-positive quantity nested inside items")
    void rejectsNonPositiveQuantity() throws Exception {
        var request = new PlaceOrderRequest("CUST-1", List.of(new OrderItem("PROD-CABLE", 0)), "1 Sidings Way",
                "CARD");

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().isBadRequest());

        verify(validationService, never()).validate(any());
    }

    private static PlaceOrderRequest validRequest() {
        return new PlaceOrderRequest("CUST-1", List.of(new OrderItem("PROD-CABLE", 4)), "1 Sidings Way", "CARD");
    }

    private String json(Object value) throws Exception {
        return objectMapper.writeValueAsString(value);
    }
}
