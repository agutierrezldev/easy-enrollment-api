package com.agutierrezl.easy_enrollment_api.repository;

import com.agutierrezl.easy_enrollment_api.model.Student;
import reactor.core.publisher.Flux;

public interface IStudentRepository extends IGenericRepository<Student, String> {

    Flux<Student> findAllByOrderByYearAsc();
    Flux<Student> findAllByOrderByYearDesc();

}
