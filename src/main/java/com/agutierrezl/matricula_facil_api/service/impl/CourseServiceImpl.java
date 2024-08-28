package com.agutierrezl.matricula_facil_api.service.impl;

import com.agutierrezl.matricula_facil_api.model.Course;
import com.agutierrezl.matricula_facil_api.repository.ICourseRepository;
import com.agutierrezl.matricula_facil_api.repository.IGenericRepository;
import com.agutierrezl.matricula_facil_api.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CRUDImpl<Course,String> implements ICourseService {

    private final ICourseRepository courseRepository;

    @Override
    protected IGenericRepository<Course, String> getRepository() {
        return courseRepository;
    }

}
