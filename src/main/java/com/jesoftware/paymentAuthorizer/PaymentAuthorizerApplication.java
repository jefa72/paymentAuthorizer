package com.jesoftware.paymentAuthorizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jesoftware.paymentAuthorizer.model.GlobalPaymentRule;
import com.jesoftware.paymentAuthorizer.model.PaymentRules;
import com.jesoftware.paymentAuthorizer.service.InputLineReader;
import com.jesoftware.paymentAuthorizer.service.PaymentObjectMapper;
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
	CommandLineRunner runPaymentAuthorizer (PaymentObjectMapper paymentObjectMapper, InputLineReader inputLineReader) {
		return args -> {

			String nextLine = inputLineReader.readNextLine();
			try {
				GlobalPaymentRule globalPaymentRule = paymentObjectMapper.deserializeJson(nextLine, GlobalPaymentRule.class);
				String globalPaymentRuleString = paymentObjectMapper.serializeObject(globalPaymentRule);
				System.out.println("Serialized object is: " + globalPaymentRuleString);
			} catch (JsonProcessingException e) {
				System.out.println("JsonProcessingException caught: " + e);
			}
		};
	}

}
