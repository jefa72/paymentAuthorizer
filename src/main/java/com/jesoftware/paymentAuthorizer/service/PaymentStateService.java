package com.jesoftware.paymentAuthorizer.service;

import com.jesoftware.paymentAuthorizer.model.PaymentState;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PaymentStateService {
    @Autowired
    private final PaymentState paymentState;

    private boolean hasGlobalRule () {
        return paymentState.getMaxLimit() > 0;
    }

    public boolean setGlobalRule (int maxLimit) {
        if (maxLimit > 0) {
            paymentState.setMaxLimit(maxLimit);
            return true;
        }
        return false;
    }

    public boolean setPaymentSession (int paymentId) {
        Set<Integer> paymentSessions = paymentState.getActivePaymentSessions();
        if (!paymentSessions.contains(paymentId)) {
            paymentSessions.add(paymentId);
            return true;
        }
        return false;
    }

    private boolean hasPaymentSession (int paymentId) {
        return paymentState.getActivePaymentSessions().contains(paymentId);
    }

    public List<String> authorizePayment (int paymentId, int amount) {
        List<String> violations = new ArrayList<>();
        if (!hasGlobalRule()) {
            violations.add(ViolationEnum.PAYMENT_RULES_NOT_INITIALIZED.name());
        }
        if (!hasPaymentSession(paymentId)) {
            violations.add(ViolationEnum.PAYMENT_SESSION_ALREADY_INITIALIZED.name());
        }
        if (violations.isEmpty() && !(paymentState.getPaymentAvailable().get(paymentId) >= amount)) {
            violations.add(ViolationEnum.INSUFICIENT_LIMIT.name());
        }
        if (violations.isEmpty()) {
            int available = paymentState.getPaymentAvailable().get(paymentId);
            paymentState.getPaymentAvailable().put(paymentId, available - amount);
        }
        return violations;
    }

}
