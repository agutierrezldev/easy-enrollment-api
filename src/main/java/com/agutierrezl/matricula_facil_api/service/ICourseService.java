package com.agutierrezl.matricula_facil_api.service;

import com.agutierrezl.matricula_facil_api.model.Course;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICourseService {

    Mono<Course> save(Course course);
    Mono<Course> update(String id, Course course);
    Flux<Course> findAll();
    Mono<Course> findById(String id);
    Mono<Boolean> delete(String id);
}
