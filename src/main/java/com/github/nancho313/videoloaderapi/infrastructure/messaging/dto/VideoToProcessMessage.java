package com.github.nancho313.videoloaderapi.infrastructure.messaging.dto;

public record VideoToProcessMessage(String videoId, String taskId) {
}
