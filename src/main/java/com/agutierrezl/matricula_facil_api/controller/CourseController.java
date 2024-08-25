package com.agutierrezl.matricula_facil_api.controller;

import com.agutierrezl.matricula_facil_api.model.Course;
import com.agutierrezl.matricula_facil_api.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Course>>> findByAll(){
        Flux<Course> courses = courseService.findAll();
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(courses))
                .thenReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<Course>>> findById(@PathVariable("id") String id){
        return courseService.findById(id)
                .map(course -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(course))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<Course>>> save(@RequestBody Course course, final ServerHttpRequest request){
        return courseService.save(course)
                .map(co->ResponseEntity.created(
                                URI.create(request.getURI().toString().concat("/").concat(course.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(co))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Course>> update(@PathVariable("id") String id, @RequestBody Course course){
        return Mono.just(course).map(
                        c -> {
                            c.setId(id);
                            return c;
                        })
                .flatMap(e->courseService.update(id,e))
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

}
