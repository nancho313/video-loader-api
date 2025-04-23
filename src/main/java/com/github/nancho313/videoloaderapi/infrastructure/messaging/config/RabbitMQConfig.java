package com.github.nancho313.videoloaderapi.infrastructure.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public Queue queue() {
    return new Queue("q-process-video", true);
  }

  @Bean
  public Exchange exchange() {
    return new DirectExchange("exchange-process-video");
  }

  @Bean
  public Binding binding(Queue queue, Exchange exchange) {
    return BindingBuilder.bind(queue)
            .to(exchange)
            .with("rk-process-video")
            .noargs();
  }
}