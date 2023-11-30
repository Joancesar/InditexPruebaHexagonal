package com.inditex.zarachallenge.infrastructure.messaging;

import java.util.Optional;
import java.util.function.Consumer;

import com.inditex.zarachallenge.application.service.SizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.inditex.zarachallenge.application.model.event.ProductAvailabilityEvent;

@Slf4j
@Component
public class KafkaListener {

	private final SizeService sizeService;

	public KafkaListener(SizeService sizeService) {
		this.sizeService = sizeService;
	}

	@Bean
	public Consumer<Message<ProductAvailabilityEvent>> kafkaConsumer() {
		return message -> {
			try {
				Optional.of(message.getPayload())
						.map(sizeService::updateSizeAvailability)
						.filter(updatedRows -> updatedRows > 0)
						.ifPresentOrElse(updatedRows ->
								log.info("Successfully entity updated: rows updated {}", updatedRows),
								() -> log.warn("Update of entity failed with message {}", message.getPayload())
						);
			} catch (Exception ex) {
				log.error("Unexpected error happened {}, with message {}", ex, message.getPayload());
			}
		};
	}

}
