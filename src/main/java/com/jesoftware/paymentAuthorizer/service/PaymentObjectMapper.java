package com.jesoftware.paymentAuthorizer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesoftware.paymentAuthorizer.model.GlobalPaymentRule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentObjectMapper {

    private final ObjectMapper objectMapper;

    public <T> T deserializeJson(String json, Class<T> tClass) throws JsonProcessingException {
        return objectMapper.readValue(json, tClass);
    }

    public <T> String serializeObject(T t) throws JsonProcessingException {
        return objectMapper.writeValueAsString(t);
    }
}
