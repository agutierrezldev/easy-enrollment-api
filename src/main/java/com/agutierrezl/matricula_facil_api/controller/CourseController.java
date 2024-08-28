package com.agutierrezl.matricula_facil_api.controller;

import com.agutierrezl.matricula_facil_api.dto.CourseDTO;
import com.agutierrezl.matricula_facil_api.model.Course;
import com.agutierrezl.matricula_facil_api.service.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<CourseDTO>>> findByAll(){
        Flux<CourseDTO> courses = courseService.findAll().map(this::convertToDTO);
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(courses))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<CourseDTO>>> findById(@PathVariable("id") String id){
        return courseService.findById(id)
                .map(this::convertToDTO)
                .map(course -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(course))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<CourseDTO>>> save(@Valid @RequestBody CourseDTO courseDTO, final ServerHttpRequest request){
        return courseService.save(this.convertToDocument(courseDTO))
                .map(this::convertToDTO)
                .map(co->ResponseEntity.created(
                                URI.create(request.getURI().toString().concat("/").concat(co.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(co))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CourseDTO>> update(@PathVariable("id") String id, @Valid @RequestBody CourseDTO courseDTO){
        return Mono.just(convertToDocument(courseDTO)).map(
                        c -> {
                            c.setId(id);
                            return c;
                        })
                .flatMap(
                        e -> courseService.update(id,e)
                ).map(this::convertToDTO)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return courseService.delete(id)
                .flatMap(result -> {
                    if(result){
                        return Mono.just(ResponseEntity.noContent().build());
                    }else{
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

    private CourseDTO convertToDTO (Course model){
        return  modelMapper.map(model,CourseDTO.class);
    }

    private Course convertToDocument (CourseDTO dto){
        return  modelMapper.map(dto,Course.class);
    }

}
