package com.agutierrezl.easy_enrollment_api.controller;

import com.agutierrezl.easy_enrollment_api.dto.StudentDTO;
import com.agutierrezl.easy_enrollment_api.model.Student;
import com.agutierrezl.easy_enrollment_api.pagination.PageSupport;
import com.agutierrezl.easy_enrollment_api.service.IStudentService;
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
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;
    @Qualifier("studentMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<StudentDTO>>> findByAll(){
        Flux<StudentDTO> Students = studentService.findAll().map(this::convertToDTO);
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Students))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<StudentDTO>>> findById(@PathVariable("id") String id){
        return studentService.findById(id)
                .map(this::convertToDTO)
                .map(Student -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(Student))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<StudentDTO>>> save(@Valid @RequestBody StudentDTO StudentDTO, final ServerHttpRequest request){
        return studentService.save(this.convertToDocument(StudentDTO))
                .map(this::convertToDTO)
                .map(co->ResponseEntity.created(
                                URI.create(request.getURI().toString().concat("/").concat(co.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(co))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<StudentDTO>> update(@PathVariable("id") String id, @Valid @RequestBody StudentDTO StudentDTO){
        return Mono.just(convertToDocument(StudentDTO)).map(
                        c -> {
                            c.setId(id);
                            return c;
                        })
                .flatMap(e ->studentService.update(id,e))
                .map(this::convertToDTO)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return studentService.delete(id)
                .flatMap(result -> {
                    if(result){
                        return Mono.just(ResponseEntity.noContent().build());
                    }else{
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }


    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<StudentDTO>>> getPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "2") int size
    ){
        return studentService.getPage(PageRequest.of(page, size))
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

    private StudentDTO StudentDtoHateoas;

    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<StudentDTO>> getHateos(@PathVariable("id") String id){
        Mono<Link> monoLink = linkTo(methodOn(StudentController.class).findById(id)).withRel("Student-info").toMono();

        // Common practice
        /* return StudentService.findById(id)
                .map(this::convertToDTO)
                .flatMap(d->{
                    this.StudentDtoHateoas = d;
                    return monoLink;
                })
                .map(link -> EntityModel.of(this.StudentDtoHateoas,link)); */

        // Intermediate practice
        /* return StudentService.findById(id)
                .map(this::convertToDTO)
                .flatMap(StudentDTO -> monoLink.map(link->EntityModel.of(StudentDTO,link))); */

        // Ideal practice
        return studentService.findById(id)
                .map(this::convertToDTO)
                .zipWith(monoLink , EntityModel::of); //(StudentDTO, link) -> EntityModel.of(StudentDTO, link)
    }

    /*
        Asc   : true
        Desc  : false
    * */
    @GetMapping("/ascending/{asc}")
    public Mono<ResponseEntity<Flux<StudentDTO>>> findAllByOrderByYearAscOrDesc(@PathVariable("asc") boolean order){
        Flux<StudentDTO> Students = studentService.findAllByOrderByYearAscOrDesc(order).map(this::convertToDTO);
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Students))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private StudentDTO convertToDTO (Student model){
        return  modelMapper.map(model,StudentDTO.class);
    }

    private Student convertToDocument (StudentDTO dto){
        return  modelMapper.map(dto,Student.class);
    }

}
