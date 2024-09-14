package com.agutierrezl.matricula_facil_api.handler;

import com.agutierrezl.matricula_facil_api.dto.CourseDTO;
import com.agutierrezl.matricula_facil_api.dto.ValidationDTO;
import com.agutierrezl.matricula_facil_api.model.Course;
import com.agutierrezl.matricula_facil_api.service.ICourseService;
import com.agutierrezl.matricula_facil_api.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class CourseHandler {

    private final ICourseService courseService;
    @Qualifier("defaultMapper")
    private final ModelMapper modelMapper;

    // private final Validator validator;

    private final RequestValidator validator;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.findAll()
                        .map(this::convertToDTO), CourseDTO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        return courseService.findById(id)
                .map(this::convertToDTO)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<CourseDTO> courseDTO = request.bodyToMono(CourseDTO.class);

//        return courseDTO
//                .flatMap(e -> {
//                    // (courseDTO , alias)
//                    Errors errors = new BeanPropertyBindingResult(e, CourseDTO.class.getName());
//                    validator.validate(e, errors); // object , block of errors that can have object e
//
//                    if (errors.hasErrors()) {
//                        return Flux.fromIterable(errors.getFieldErrors())
//                                .map(error -> new ValidationDTO(error.getField(), error.getDefaultMessage()))
//                                .collectList()
//                                .flatMap(list -> ServerResponse
//                                        .badRequest()
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .body(fromValue(list)));
//
//                    } else {
//                        return courseService.save(convertToDocument(e))
//                                .map(this::convertToDTO)
//                                .flatMap(dto -> ServerResponse
//                                        .created(URI.create(request.uri().toString().concat("/").concat(dto.getId())))
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .body(fromValue(dto)));
//                    }
//                });

        return courseDTO
                .flatMap(validator::validate)
                .flatMap(e -> courseService.save(convertToDocument(e)))
                .map(this::convertToDTO)
                .flatMap(e -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)));


//        return courseDTO
//                .flatMap(e -> courseService.save(convertToDocument(e)))
//                .map(this::convertToDTO)
//                .flatMap(e -> ServerResponse
//                        .created(URI.create(request.uri().toString().concat("/").concat(e.getId())))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(fromValue(e)));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<CourseDTO> courseDTO = request.bodyToMono(CourseDTO.class);
        return courseDTO
                .flatMap(validator::validate)
                .map(c -> {
                    c.setId(id);
                    return c;
                })
                .flatMap(e -> courseService.update(id, convertToDocument(e)))
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
        return courseService.delete(id)
                .flatMap(result -> {
                    if (result) {
                        return ServerResponse.noContent().build();
                    } else {
                        return ServerResponse.notFound().build();
                    }
                });
    }


    private CourseDTO convertToDTO(Course model) {
        return modelMapper.map(model, CourseDTO.class);
    }

    private Course convertToDocument(CourseDTO dto) {
        return modelMapper.map(dto, Course.class);
    }

}
