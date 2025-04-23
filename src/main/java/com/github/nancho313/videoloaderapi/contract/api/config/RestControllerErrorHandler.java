package com.github.nancho313.videoloaderapi.contract.api.config;

import com.github.nancho313.videoloaderapi.business.exception.InvalidCredentialsException;
import com.github.nancho313.videoloaderapi.contract.api.dto.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class RestControllerErrorHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorMessageResponse badRequest(Exception ex, WebRequest request) {
    return new ErrorMessageResponse(ex.getMessage());
  }

  @ExceptionHandler(value = {NoSuchElementException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorMessageResponse notFound(Exception ex, WebRequest request) {
    return new ErrorMessageResponse(ex.getMessage());
  }

  @ExceptionHandler(value = {InvalidCredentialsException.class})
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public ErrorMessageResponse unauthorized(Exception ex, WebRequest request) {
    return new ErrorMessageResponse(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
          MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
