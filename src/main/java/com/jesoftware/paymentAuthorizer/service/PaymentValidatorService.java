package com.jesoftware.paymentAuthorizer.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class PaymentValidatorService {


    public boolean validatePaymentSession(Set<Integer> paymentSessionIds, Integer paymentSessionId, List<String> violations) {
        if (!paymentSessionIds.contains(paymentSessionId)) {
            return true;
        }
        violations.add(ViolationEnum.PAYMENT_SESSION_ALREADY_INITIALIZED.name());
        return false;
    }

    public boolean isPresentGlobalRule(Integer maxLimit, List<String> violations) {
        if (Objects.nonNull(maxLimit)) {
            return true;
        }
        violations.add(ViolationEnum.PAYMENT_RULES_NOT_INITIALIZED.name());
        return false;
    }

    public boolean isPresentPaymentSession(Set<Integer> paymentSessionIds, Integer paymentSessionId, List<String> violations) {
        if (paymentSessionIds.contains(paymentSessionId)) {
            return true;
        }
        violations.add(ViolationEnum.PAYMENT_SESSION_NOT_INITIALIZED.name());
        return false;
    }

    public boolean hasEnoughAvailableAmount(int availableAmount, int amount, List<String> violations) {
        if (availableAmount >= amount) {
            return true;
        }
        violations.add(ViolationEnum.INSUFICIENT_LIMIT.name());
        return false;
    }

}
