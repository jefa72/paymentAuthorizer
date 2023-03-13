package com.jesoftware.paymentAuthorizer.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jesoftware.paymentAuthorizer.model.PaymentSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class PaymentAuthorizationResult {

    private PaymentAuthorizationDetailsResult paymentSession;
    private List<String> violations;

}
