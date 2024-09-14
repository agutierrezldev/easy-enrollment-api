package com.agutierrezl.matricula_facil_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomError {

    private String message;
    private String status;
    private HttpStatus httpStatus;
    private List<CustomErrorInput> errors ;
}
