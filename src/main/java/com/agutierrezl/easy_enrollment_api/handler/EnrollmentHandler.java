package com.agutierrezl.easy_enrollment_api.handler;

import com.agutierrezl.easy_enrollment_api.dto.EnrollmentDTO;
import com.agutierrezl.easy_enrollment_api.model.Enrollment;
import com.agutierrezl.easy_enrollment_api.service.IEnrollmentService;
import com.agutierrezl.easy_enrollment_api.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class EnrollmentHandler {

    private final IEnrollmentService enrollmentService;
    @Qualifier("enrollmentMapper")
    private final ModelMapper modelMapper;

    private final RequestValidator validator;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(enrollmentService.findAll()
                        .map(this::convertToDTO), EnrollmentDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return enrollmentService.findById(id)
                .map(this::convertToDTO)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<EnrollmentDTO> EnrollmentDTO = request.bodyToMono(EnrollmentDTO.class);
        return EnrollmentDTO
                .flatMap(validator::validate)
                .flatMap(e -> enrollmentService.save(convertToDocument(e)))
                .map(this::convertToDTO)
                .flatMap(e -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<EnrollmentDTO> EnrollmentDTO = request.bodyToMono(EnrollmentDTO.class);
        return EnrollmentDTO
                .flatMap(validator::validate)
                .map(c -> {
                    c.setId(id);
                    return c;
                })
                .flatMap(e -> enrollmentService.update(id, convertToDocument(e)))
                .map(this::convertToDTO)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return enrollmentService.delete(id)
                .flatMap(result -> {
                    if (result) {
                        return ServerResponse.noContent().build();
                    } else {
                        return ServerResponse.notFound().build();
                    }
                });
    }


    private EnrollmentDTO convertToDTO(Enrollment model) {
        return modelMapper.map(model, EnrollmentDTO.class);
    }

    private Enrollment convertToDocument(EnrollmentDTO dto) {
        return modelMapper.map(dto, Enrollment.class);
    }

}
