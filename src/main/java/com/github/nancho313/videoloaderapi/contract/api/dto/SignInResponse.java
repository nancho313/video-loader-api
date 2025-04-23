package com.github.nancho313.videoloaderapi.contract.api.dto;

public record SignInResponse(String accessToken, String tokenType, Integer expiresIn) {
}
