package com.joevullo.switchyard.orders;

import java.math.BigDecimal;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "switchyard.orders")
public record OrderProperties(
    BigDecimal maxOrderTotal, 
    String topic) 
    { }
