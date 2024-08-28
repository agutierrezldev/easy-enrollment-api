package com.agutierrezl.matricula_facil_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomError {

    private String message;
    private int status;
    private HttpStatus httpStatus;

}
