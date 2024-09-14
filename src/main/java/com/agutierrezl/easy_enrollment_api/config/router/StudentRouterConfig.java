package com.agutierrezl.easy_enrollment_api.config.router;

import com.agutierrezl.easy_enrollment_api.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StudentRouterConfig {

    @Bean(name = "routeStudent")
    public RouterFunction<ServerResponse> routes(StudentHandler handler) {
        return route(GET("v2/students"), handler::findAll) // req -> handler.findAll(req
                .andRoute(GET("v2/students/{id}"), handler::findById)
                .andRoute(POST("v2/students"), handler::save)
                .andRoute(PUT("v2/students/{id}"), handler::update)
                .andRoute(DELETE("v2/students/{id}"), handler::delete);
    }
}
