package com.jesoftware.paymentAuthorizer.service;

import com.jesoftware.paymentAuthorizer.model.PaymentAuthorizerState;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Data
@AllArgsConstructor
public class PaymentStateService {

    @Autowired
    private final PaymentAuthorizerState paymentAuthorizerState;

    @Autowired
    private final PaymentValidatorService paymentValidatorService;

    public void setGlobalRule (int maxLimit) {
            paymentAuthorizerState.setMaxLimit(maxLimit);
    }

    public void setPaymentSession (int paymentId) {
        Set<Integer> paymentSessions = paymentAuthorizerState.getActivePaymentSessions();
        if (!paymentAuthorizerState.getViolations().containsKey(paymentId)) {
            paymentAuthorizerState.getViolations().put(paymentId, new ArrayList<>());
        }
        if (!paymentAuthorizerState.getAvailablePayments().containsKey(paymentId)) {
            paymentAuthorizerState.getAvailablePayments().put(paymentId,paymentAuthorizerState.getMaxLimit());
        }
        List<String> violations = paymentAuthorizerState.getViolations().get(paymentId);
        boolean isPaymentSessionValid = paymentValidatorService.validatePaymentSession(paymentSessions, paymentId, violations);
        if (isPaymentSessionValid) {
            paymentSessions.add(paymentId);
        }
    }

    public int getAvailableAmount(int paymentId) {
        return paymentAuthorizerState.getAvailablePayments().get(paymentId);
    }

    public List<String> getViolations(int paymentId) {
        return paymentAuthorizerState.getViolations().get(paymentId);
    }

    public List<String> authorizePayment (int paymentId, int amount) {
        if (!paymentAuthorizerState.getViolations().containsKey(paymentId)) {
            paymentAuthorizerState.getViolations().put(paymentId, new ArrayList<>());
        }
        List<String> violations = paymentAuthorizerState.getViolations().get(paymentId);
        boolean isPresentGlobalRule = paymentValidatorService.isPresentGlobalRule(paymentAuthorizerState.getMaxLimit(), violations);

        Set<Integer> paymentSessions = paymentAuthorizerState.getActivePaymentSessions();
        boolean isPresentPaymentSession = paymentValidatorService.isPresentPaymentSession(paymentSessions, paymentId, violations);

        if (!isPresentGlobalRule || !isPresentPaymentSession) {
            return violations;
        }

        int availableAount = paymentAuthorizerState.getAvailablePayments().get(paymentId);
        boolean hasEnoughAvailableAount = paymentValidatorService.hasEnoughAvailableAmount(availableAount, amount, violations);

        if (hasEnoughAvailableAount) {
            paymentAuthorizerState.getAvailablePayments().put(paymentId, availableAount - amount);
        }
        return violations;
    }

}
