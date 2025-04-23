package com.github.nancho313.videoloaderapi.contract.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(@NotBlank(message = "The email cannot be empty.") String email,
                            @NotBlank(message = "The password cannot be empty.") String password) {
}
