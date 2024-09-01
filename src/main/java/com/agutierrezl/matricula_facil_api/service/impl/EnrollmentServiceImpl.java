package com.agutierrezl.matricula_facil_api.service.impl;

import com.agutierrezl.matricula_facil_api.model.Enrollment;
import com.agutierrezl.matricula_facil_api.model.EnrollmentDetail;
import com.agutierrezl.matricula_facil_api.repository.ICourseRepository;
import com.agutierrezl.matricula_facil_api.repository.IEnrollmentRepository;
import com.agutierrezl.matricula_facil_api.repository.IGenericRepository;
import com.agutierrezl.matricula_facil_api.repository.IStudentRepository;
import com.agutierrezl.matricula_facil_api.service.IEnrollmentService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl extends CRUDImpl<Enrollment, String> implements IEnrollmentService {

    private final IEnrollmentRepository enrollmentRepository;
    private final IStudentRepository studentRepository;
    private final ICourseRepository courseRepository;

    @Override
    protected IGenericRepository<Enrollment, String> getRepository() {
        return enrollmentRepository;
    }

    private String getRuteImage(String img) throws IOException {
        String rute = "assets/".concat(img);
        ClassPathResource resource = new ClassPathResource(rute);
        return resource.getFile().toString();
    }

    private Mono<Enrollment> populateStudent(Enrollment enrollment) {
        return studentRepository.findById(enrollment.getStudent().getId())
                .map(student -> {
                    enrollment.setStudent(student);
                    return enrollment;
                });
    }

    private Mono<Enrollment> populateItems(Enrollment enrollment) {
        List<Mono<EnrollmentDetail>> list = enrollment.getItems().stream() // Stream List<EnrollmentDetail>
                .map(item -> courseRepository.findById(item.getCourse().getId()) // Mono<Course>
                        .map(course -> {
                            item.setCourse(course);
                            return item;
                        })
                ).toList();
        return Mono.when(list).then(Mono.just(enrollment));
    }

    private byte[] generatePDF(Enrollment enrollment) {
        try {
            Map<String, Object> params = new HashMap<>();
            // REPORT
            params.put("txt_report_logo", getRuteImage("logo.png"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            params.put("txt_report_date", LocalDateTime.now().format(formatter));

            // STUDENT
            params.put("txt_student_name", enrollment.getStudent().getFirstName());
            params.put("txt_student_lastname", enrollment.getStudent().getLastName());
            params.put("txt_student_dni", enrollment.getStudent().getDni());
            params.put("txt_student_year", enrollment.getStudent().getYear().toString());
            params.put("txt_student_image", getRuteImage("student.png"));

            InputStream jrxml = getClass().getResourceAsStream("/Enrollment.jrxml");
            JasperReport jasper = JasperCompileManager.compileReport(jrxml);
            //JasperPrint print = JasperFillManager.fillReport(jasper, params, new JREmptyDataSource());
            JasperPrint print = JasperFillManager.fillReport(jasper, params, new JRBeanCollectionDataSource(enrollment.getItems()));

            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            return new byte[0];
        }
    }


    @Override
    public Mono<byte[]> generteReport(String idEnrollment) {
        return enrollmentRepository.findById(idEnrollment)
                .flatMap(this::populateStudent)
                .flatMap(this::populateItems)
                .map(this::generatePDF)
                .onErrorResume(e -> Mono.empty());
    }

//    @Override
//    public Mono<byte[]> generteReport(String idEnrollment) {
//        return enrollmentRepository.findById(idEnrollment)
//                // Get Student
//                .flatMap(e -> Mono.just(e)
//                        .zipWith(studentRepository.findById(e.getStudent().getId()) , (enroll, stud) -> {
//                            enroll.setStudent(stud);
//                            return enroll;
//                        })
//                )
//                //Get Courses
//                .flatMap(enrollment -> {
//                    return Flux.fromIterable(enrollment.getItems())
//                            .flatMap(item -> {
//                                return courseRepository.findById(item.getCourse().getId())
//                                        .map( course -> {
//                                            item.setCourse(course);
//                                            return item;
//                                        });
//                            }).collectList()
//                            .flatMap(list -> {
//                                enrollment.setItems(list);
//                                return Mono.just(enrollment);
//                            });
//                })
//                .map(enrollment -> {
//                    try {
//                        Map<String, Object> params = new HashMap<>();
//                        // REPORT
//                        params.put("txt_report_logo",getRuteImage("logo.png"));
//
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                        params.put("txt_report_date", LocalDateTime.now().format(formatter));
//
//                        // STUDENT
//                        params.put("txt_student_name", enrollment.getStudent().getFirstName());
//                        params.put("txt_student_lastname", enrollment.getStudent().getLastName());
//                        params.put("txt_student_dni", enrollment.getStudent().getDni());
//                        params.put("txt_student_year", enrollment.getStudent().getYear().toString());
//                        params.put("txt_student_image",getRuteImage("student.png"));
//
//                        InputStream jrxml = getClass().getResourceAsStream("/Enrollment.jrxml");
//                        JasperReport jasper = JasperCompileManager.compileReport(jrxml);
//                        //JasperPrint print = JasperFillManager.fillReport(jasper, params, new JREmptyDataSource());
//                        JasperPrint print = JasperFillManager.fillReport(jasper, params, new JRBeanCollectionDataSource(enrollment.getItems()));
//
//                        return JasperExportManager.exportReportToPdf(print);
//                    } catch (Exception e) {
//                        return new byte[0];
//                    }
//                });
//    }
}
