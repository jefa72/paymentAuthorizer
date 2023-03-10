package com.jesoftware.paymentAuthorizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jesoftware.paymentAuthorizer.model.*;
import com.jesoftware.paymentAuthorizer.service.InputLineReader;
import com.jesoftware.paymentAuthorizer.service.PaymentObjectMapper;
import com.jesoftware.paymentAuthorizer.service.PaymentStateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

				} catch (JsonProcessingException e1) {
					try {

						PaymentSessionCreate paymentSessionCreate =
								paymentObjectMapper.deserializeJson(nextLine, PaymentSessionCreate.class);
						paymentStateService.setPaymentSession(paymentSessionCreate.getPaymentSession().getPaymentId());

					} catch (JsonProcessingException e2) {



					}


				}
			}



		};
	}

}
