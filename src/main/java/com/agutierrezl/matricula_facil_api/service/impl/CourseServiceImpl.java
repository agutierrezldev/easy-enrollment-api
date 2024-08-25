package com.agutierrezl.matricula_facil_api.service.impl;

import com.agutierrezl.matricula_facil_api.model.Course;
import com.agutierrezl.matricula_facil_api.repository.ICourseRepository;
import com.agutierrezl.matricula_facil_api.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final ICourseRepository courseRepository;

    @Override
    public Mono<Course> save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Mono<Course> update(String id, Course course) {
        course.setId(id);
        return courseRepository.save(course);
    }

    @Override
    public Flux<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Mono<Course> findById(String id) {
        return courseRepository.findById(id);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return courseRepository.deleteById(id).then(Mono.just(true));
    }
}
