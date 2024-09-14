package com.agutierrezl.easy_enrollment_api.config.router;

import com.agutierrezl.easy_enrollment_api.handler.EnrollmentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class EnrollmentRouterConfig {

    @Bean(name = "routeEnrollment")
    public RouterFunction<ServerResponse> routes(EnrollmentHandler handler) {
        return route(GET("v2/enrollments"), handler::findAll) // req -> handler.findAll(req
                .andRoute(GET("v2/enrollments/{id}"), handler::findById)
                .andRoute(POST("v2/enrollments"), handler::save)
                .andRoute(PUT("v2/enrollments/{id}"), handler::update)
                .andRoute(DELETE("v2/enrollments/{id}"), handler::delete);
    }
}
