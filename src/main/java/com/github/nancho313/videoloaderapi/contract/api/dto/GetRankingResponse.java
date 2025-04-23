package com.github.nancho313.videoloaderapi.contract.api.dto;

public record GetRankingResponse(int position,
                                 String name,
                                 String email,
                                 String city,
                                 String country,
                                 int votes) {
}
