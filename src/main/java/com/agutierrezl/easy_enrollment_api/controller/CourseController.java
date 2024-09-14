package com.agutierrezl.easy_enrollment_api.controller;

import com.agutierrezl.easy_enrollment_api.dto.CourseDTO;
import com.agutierrezl.easy_enrollment_api.model.Course;
import com.agutierrezl.easy_enrollment_api.pagination.PageSupport;
import com.agutierrezl.easy_enrollment_api.service.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;
    @Qualifier("defaultMapper")
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
                .flatMap(e ->courseService.update(id,e))
                .map(this::convertToDTO)
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


    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<CourseDTO>>> getPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "2") int size
    ){
        return courseService.getPage(PageRequest.of(page, size))
                .map(pageSupport -> new PageSupport<>(
                        pageSupport.getContent().stream().map(this::convertToDTO).toList(),
                        pageSupport.getPageNumber(),
                        pageSupport.getPageSize(),
                        pageSupport.getTotalElements()
                ))
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private CourseDTO courseDtoHateoas;

    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<CourseDTO>> getHateos(@PathVariable("id") String id){
        Mono<Link> monoLink = linkTo(methodOn(CourseController.class).findById(id)).withRel("course-info").toMono();

        // Common practice
        /* return courseService.findById(id)
                .map(this::convertToDTO)
                .flatMap(d->{
                    this.courseDtoHateoas = d;
                    return monoLink;
                })
                .map(link -> EntityModel.of(this.courseDtoHateoas,link)); */

        // Intermediate practice
        /* return courseService.findById(id)
                .map(this::convertToDTO)
                .flatMap(courseDTO -> monoLink.map(link->EntityModel.of(courseDTO,link))); */

        // Ideal practice
        return courseService.findById(id)
                .map(this::convertToDTO)
                .zipWith(monoLink , EntityModel::of); //(courseDTO, link) -> EntityModel.of(courseDTO, link)
    }

    private CourseDTO convertToDTO (Course model){
        return  modelMapper.map(model,CourseDTO.class);
    }

    private Course convertToDocument (CourseDTO dto){
        return  modelMapper.map(dto,Course.class);
    }

}
