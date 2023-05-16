package dev.muriloabranches.avaliacao.controller.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Data
public class ErrorResponse {

    private HttpStatus status;
    private List<String> errors;

    public ErrorResponse(HttpStatus status, List<String> errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public ErrorResponse(HttpStatus status, String error) {
        super();
        this.status = status;
        errors = Collections.singletonList(error);
    }
}
