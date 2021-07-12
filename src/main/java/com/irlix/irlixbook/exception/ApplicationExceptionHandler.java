package com.irlix.irlixbook.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({PasswordActiveException.class})
    protected ResponseEntity<ApiError> handlePasswordActiveException(PasswordActiveException ex) {
        ApiError apiError = new ApiError("Exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.TEMPORARY_REDIRECT);
    }

    @ExceptionHandler({ConflictException.class})
    protected ResponseEntity<ApiError> handlePasswordActiveException(ConflictException ex) {
        ApiError apiError = new ApiError("Exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<ApiError> handleNotFoundException(BadRequestException ex) {
        ApiError apiError = new ApiError("Exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MultipartException.class})
    protected ResponseEntity<ApiError> handlePasswordActiveException(MultipartException ex) {
        ApiError apiError = new ApiError("Exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        ApiError apiError = new ApiError("Exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UnauthorizedException.class})
    protected ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException ex) {
        ApiError apiError = new ApiError("Unauthorized exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ForbiddenException.class})
    protected ResponseEntity<ApiError> handleForbiddenException(ForbiddenException ex) {
        ApiError apiError = new ApiError("Forbidden exception", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError("Malformed JSON Request", ex.getMessage());
        return new ResponseEntity(apiError, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        ApiError apiError = new ApiError("Method Argument Not Valid" + errors);
        return new ResponseEntity<>(apiError, status);
    }
}
