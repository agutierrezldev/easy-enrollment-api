package com.agutierrezl.matricula_facil_api.service.impl;

import com.agutierrezl.matricula_facil_api.model.Course;
import com.agutierrezl.matricula_facil_api.model.Enrollment;
import com.agutierrezl.matricula_facil_api.repository.IEnrollmentRepository;
import com.agutierrezl.matricula_facil_api.repository.IGenericRepository;
import com.agutierrezl.matricula_facil_api.service.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl extends CRUDImpl<Enrollment, String> implements IEnrollmentService {

    private final IEnrollmentRepository enrollmentRepository;

    @Override
    protected IGenericRepository<Enrollment, String> getRepository() {
        return enrollmentRepository;
    }

    @Override
    public Mono<byte[]> generteReport(String idEnrollment) {
        return enrollmentRepository.findById(idEnrollment)
                .map(enrollment -> {
                    try {
                        Map<String, Object> params = new HashMap<>();
                        params.put("txt_student", enrollment.getStudent().getId());

                        InputStream jrxml = getClass().getResourceAsStream("/Enrollment.jrxml");
                        JasperReport jasper = JasperCompileManager.compileReport(jrxml);
                        JasperPrint print = JasperFillManager.fillReport(jasper, params, new JREmptyDataSource());

                        return JasperExportManager.exportReportToPdf(print);
                    } catch (Exception e) {
                        return new byte[0];
                    }
                });
    }
}
