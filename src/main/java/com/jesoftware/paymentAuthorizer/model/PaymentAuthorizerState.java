package com.jesoftware.paymentAuthorizer.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Data
@Component
public final class PaymentAuthorizerState {
    private Integer maxLimit;
    private final Set<Integer> activePaymentSessions = new HashSet<>();
    private final Map<Integer, Integer> availablePayments = new HashMap<>();
    private final Map<Integer, List<String>> violations = new HashMap<>();
}
