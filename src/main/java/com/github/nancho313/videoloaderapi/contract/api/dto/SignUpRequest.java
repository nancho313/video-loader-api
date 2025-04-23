package com.github.nancho313.videoloaderapi.contract.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(@NotBlank(message = "The name cannot be empty.") String name,
                            @NotBlank(message = "The lastname cannot be empty.") String lastname,
                            @NotBlank(message = "The email cannot be empty.")
                            @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "The email is invalid.")
                            String email,
                            @NotBlank(message = "The city cannot be empty.") String city,
                            @NotBlank(message = "The country cannot be empty.") String country,
                            @NotBlank(message = "The password1 cannot be empty.") String password1,
                            @NotBlank(message = "The password1 cannot be empty.") String password2) {
}
