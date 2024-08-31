package com.agutierrezl.matricula_facil_api.service;

import com.agutierrezl.matricula_facil_api.model.Enrollment;
import reactor.core.publisher.Mono;

public interface IEnrollmentService extends ICRUD<Enrollment, String> {

    Mono<byte[]> generteReport(String idEnrollment);
}
