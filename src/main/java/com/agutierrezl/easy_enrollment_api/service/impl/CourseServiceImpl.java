package com.agutierrezl.easy_enrollment_api.service.impl;

import com.agutierrezl.easy_enrollment_api.model.Course;
import com.agutierrezl.easy_enrollment_api.repository.ICourseRepository;
import com.agutierrezl.easy_enrollment_api.repository.IGenericRepository;
import com.agutierrezl.easy_enrollment_api.service.ICourseService;
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
