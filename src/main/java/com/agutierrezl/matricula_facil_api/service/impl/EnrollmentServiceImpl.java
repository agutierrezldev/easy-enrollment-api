package com.agutierrezl.matricula_facil_api.service.impl;

import com.agutierrezl.matricula_facil_api.model.Enrollment;
import com.agutierrezl.matricula_facil_api.repository.IEnrollmentRepository;
import com.agutierrezl.matricula_facil_api.repository.IGenericRepository;
import com.agutierrezl.matricula_facil_api.service.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl extends CRUDImpl<Enrollment,String> implements IEnrollmentService {

    private final IEnrollmentRepository enrollmentRepository;

    @Override
    protected IGenericRepository<Enrollment, String> getRepository() {
        return enrollmentRepository;
    }

}
