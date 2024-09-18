package com.agutierrezl.easy_enrollment_api.service;

import com.agutierrezl.easy_enrollment_api.model.Student;
import reactor.core.publisher.Flux;

public interface IStudentService extends ICRUD<Student, String> {
    Flux<Student> findAllByOrderByYearAscOrDesc(boolean order);
}
