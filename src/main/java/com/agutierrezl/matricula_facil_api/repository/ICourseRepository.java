package com.agutierrezl.matricula_facil_api.repository;

import com.agutierrezl.matricula_facil_api.model.Course;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICourseRepository extends ReactiveMongoRepository<Course, String> {
}
