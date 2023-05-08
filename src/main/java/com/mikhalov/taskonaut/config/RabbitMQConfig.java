package com.mikhalov.taskonaut.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    DirectExchange notesExchange() {
        return new DirectExchange("telegram", true, false);
    }

    @Bean
    public Queue telegramQueue() {
        return new Queue("note-to-telegram", true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(telegramQueue())
                .to(notesExchange())
                .with("note");
    }
}
