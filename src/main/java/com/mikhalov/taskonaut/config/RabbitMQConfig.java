package com.mikhalov.taskonaut.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.telegram_exchange}")
    private String telegramExchange;
    @Value("${spring.rabbitmq.note_to_telegram_queue}")
    private String noteToTelegramQueue;
    @Value("${spring.rabbitmq.note_routing_key}")
    private String noteRoutingKey;

    @Bean
    DirectExchange telegramExchange() {
        return new DirectExchange(telegramExchange, true, false);
    }

    @Bean
    public Queue noteToTelegramQueue() {
        return new Queue(noteToTelegramQueue, true);
    }

    @Bean
    public Binding noteToTelegramBinging() {
        return BindingBuilder
                .bind(noteToTelegramQueue())
                .to(telegramExchange())
                .with(noteRoutingKey);
    }

}
