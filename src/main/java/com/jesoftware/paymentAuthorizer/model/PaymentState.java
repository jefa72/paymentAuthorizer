package com.jesoftware.paymentAuthorizer.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Component
public class PaymentState {
    private Integer maxLimit;
    private final Set<Integer> activePaymentSessions = new HashSet<>();
    private final Map<Integer, Integer> paymentAvailable = new HashMap<>();
}
