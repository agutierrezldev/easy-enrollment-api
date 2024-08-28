package com.agutierrezl.matricula_facil_api.service;

import com.agutierrezl.matricula_facil_api.model.Course;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICourseService extends ICRUD<Course, String> {

}
