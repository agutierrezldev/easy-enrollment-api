package com.agutierrezl.matricula_facil_api.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
//@Order(-1)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public WebExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest req) {

        Map<String, Object> generalError = getErrorAttributes(req, ErrorAttributeOptions.defaults());

        String statusCode = generalError.get("status").toString();
        Throwable error = getError(req);
        // HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        CustomError customError = new CustomError(error.getMessage(), "418", HttpStatus.I_AM_A_TEAPOT, null);

        switch (statusCode) {
            case "400" -> {
                List<CustomErrorInput> errors = new CustomErrorInput().getErrors(error.getMessage());
                customError.setMessage(!errors.isEmpty() ? HttpStatus.BAD_REQUEST.toString() : error.getMessage());
                customError.setErrors(!errors.isEmpty() ? errors : null);
                customError.setStatus(statusCode);
                customError.setHttpStatus(HttpStatus.BAD_REQUEST);
            }
            case "402" -> {
                /* customError.put("message",error.getMessage());
                customError.put("status", statusCode);
                httpStatus = HttpStatus.BAD_REQUEST; */
                customError.setStatus(statusCode);
                customError.setHttpStatus(HttpStatus.BAD_REQUEST);
            }
            case "404" -> {
                customError.setStatus(statusCode);
                customError.setHttpStatus(HttpStatus.NOT_FOUND);
            }
            case "401", "403" -> {
                customError.setStatus(statusCode);
                customError.setHttpStatus(HttpStatus.UNAUTHORIZED);
            }
            case "500" -> {
                customError.setStatus(statusCode);
                customError.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        return ServerResponse.status(customError.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customError));
    }


}
