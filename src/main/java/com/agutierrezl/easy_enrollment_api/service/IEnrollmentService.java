package com.agutierrezl.easy_enrollment_api.service;

import com.agutierrezl.easy_enrollment_api.model.Enrollment;
import reactor.core.publisher.Mono;

public interface IEnrollmentService extends ICRUD<Enrollment, String> {

    Mono<byte[]> generteReport(String idEnrollment);
}
