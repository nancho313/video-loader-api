package com.github.nancho313.videoloaderapi.contract.api.dto;

import java.time.LocalDateTime;

public record GetVideoResponse(String videoId,
                               String title,
                               String status,
                               LocalDateTime uploadedAt,
                               LocalDateTime processedAt,
                               String processedUrl) {
}
