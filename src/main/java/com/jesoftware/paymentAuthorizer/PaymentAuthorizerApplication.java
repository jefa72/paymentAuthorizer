package com.jesoftware.paymentAuthorizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jesoftware.paymentAuthorizer.model.*;
import com.jesoftware.paymentAuthorizer.model.output.*;
import com.jesoftware.paymentAuthorizer.service.InputLineReader;
import com.jesoftware.paymentAuthorizer.service.PaymentObjectMapper;
import com.jesoftware.paymentAuthorizer.service.PaymentStateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PaymentAuthorizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentAuthorizerApplication.class, args);
	}

	@Bean
	CommandLineRunner runPaymentAuthorizer (PaymentObjectMapper paymentObjectMapper,
											InputLineReader inputLineReader, PaymentStateService paymentStateService) {
		return args -> {

			while (inputLineReader.hasNextLine()) {
				String nextLine = inputLineReader.readNextLine();
				try {
					GlobalPaymentRule globalPaymentRule =
							paymentObjectMapper.deserializeJson(nextLine, GlobalPaymentRule.class);
					paymentStateService.setGlobalRule(globalPaymentRule.getPaymentRules().getMaxLimit());

					PaymentRulesResult paymentRulesResult = new PaymentRulesResult(globalPaymentRule.getPaymentRules().getMaxLimit());
					GlobalPaymentRuleResult globalPaymentRuleResult = new GlobalPaymentRuleResult(paymentRulesResult);
					System.out.println(paymentObjectMapper.serializeObject(globalPaymentRuleResult));

				} catch (JsonProcessingException e1) {
					try {
						PaymentSessionCreate paymentSessionCreate =
								paymentObjectMapper.deserializeJson(nextLine, PaymentSessionCreate.class);
						paymentStateService.setPaymentSession(paymentSessionCreate.getPaymentSession().getPaymentId());

						int paymentId = paymentSessionCreate.getPaymentSession().getPaymentId();
						int availableLimit = paymentStateService.getAvailableAmount(paymentId);
						PaymentSessionResult paymentSessionResult = new PaymentSessionResult(paymentId, availableLimit);
						PaymentSessionCreateResult paymentSessionCreateResult = new PaymentSessionCreateResult(paymentSessionResult,
								paymentStateService.getViolations(paymentId));
						System.out.println(paymentObjectMapper.serializeObject(paymentSessionCreateResult));

					} catch (JsonProcessingException e2) {

						try {
							PaymentSessionAuthorization paymentSessionAuthorization =
									paymentObjectMapper.deserializeJson(nextLine, PaymentSessionAuthorization.class);
							PaymentSessionDetails paymentSessionDetails = paymentSessionAuthorization.getPaymentSession();
							List<String> violations = paymentStateService.authorizePayment(paymentSessionDetails.getPaymentId(),
									paymentSessionDetails.getAmount());

							int paymentId = paymentSessionDetails.getPaymentId();
							int availableAmount;
							if (violations.isEmpty()) {
								availableAmount = paymentStateService.getAvailableAmount(paymentId);
							} else {
								availableAmount = 0;
							}
							PaymentAuthorizationDetailsResult paymentAuthorizationDetailsResult =
									new PaymentAuthorizationDetailsResult(paymentId, availableAmount);
							PaymentAuthorizationResult paymentAuthorizationResult =
									new PaymentAuthorizationResult(paymentAuthorizationDetailsResult,
											violations);
							System.out.println(paymentObjectMapper.serializeObject(paymentAuthorizationResult));

						} catch (JsonProcessingException e3) {
							System.out.println("End of processing.");
							break;
						}
					}
				}
			}
		};
	}

}
