package com.agutierrezl.matricula_facil_api.validator;

import com.agutierrezl.matricula_facil_api.exception.CustomError;
import com.agutierrezl.matricula_facil_api.exception.CustomErrorInput;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validate(T t) {

        if (t == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        }

        Set<ConstraintViolation<T>> constraints = validator.validate(t);

        if (constraints == null || constraints.isEmpty()) {
            return Mono.just(t);
        }

        List<CustomErrorInput> errors = new ArrayList<>();

        constraints
                .forEach(constraint -> {
                    CustomErrorInput error  = new CustomErrorInput();
                    error.setField(constraint.getPropertyPath().toString());
                    error.setMessage(constraint.getMessage());
                    errors.add(error);
                });

        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.toString()));

    }

}
