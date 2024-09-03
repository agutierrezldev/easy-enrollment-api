package com.agutierrezl.matricula_facil_api.config.router;

import com.agutierrezl.matricula_facil_api.handler.CourseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CourseRouterConfig {

    @Bean(name = "routeCourse")
    public RouterFunction<ServerResponse> routes(CourseHandler handler) {
        return route(GET("v2/courses"), handler::findAll) // req -> handler.findAll(req
                .andRoute(GET("v2/courses/{id}"), handler::findById)
                .andRoute(POST("v2/courses"), handler::save)
                .andRoute(PUT("v2/courses/{id}"), handler::update)
                .andRoute(DELETE("v2/courses/{id}"), handler::delete);
    }
}
