package com.github.nancho313.videoloaderapi.infrastructure.messaging.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nancho313.videoloaderapi.infrastructure.messaging.dto.VideoToProcessMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotifyVideoToProcessPublisher {

  private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

  private final RabbitTemplate rabbitTemplate;

  @SneakyThrows
  public void sentVideoToProcess(VideoToProcessMessage videoToProcessMessage) {

    this.sendMessage(JSON_MAPPER.writeValueAsString(videoToProcessMessage));
  }

  private void sendMessage(String message) {
    rabbitTemplate.convertAndSend("exchange-process-video", "rk-process-video", message);
  }
}
