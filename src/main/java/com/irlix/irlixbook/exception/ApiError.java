package com.irlix.irlixbook.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {

    private String message;

    private String debugMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(String message, String debugMessage) {
        this.message = message;
        this.debugMessage = debugMessage;
    }

    public ApiError(String message, String debugMessage, List<String> errors) {
        this.message = message;
        this.debugMessage = debugMessage;
        this.errors = errors;
    }
}
