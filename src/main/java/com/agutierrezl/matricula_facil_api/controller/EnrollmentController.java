package com.agutierrezl.matricula_facil_api.controller;

import com.agutierrezl.matricula_facil_api.dto.EnrollmentDTO;
import com.agutierrezl.matricula_facil_api.model.Enrollment;
import com.agutierrezl.matricula_facil_api.pagination.PageSupport;
import com.agutierrezl.matricula_facil_api.service.IEnrollmentService;
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
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final IEnrollmentService enrollmentService;
    @Qualifier("enrollmentMapper")
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<EnrollmentDTO>>> findByAll(){
        Flux<EnrollmentDTO> Enrollments = enrollmentService.findAll().map(this::convertToDTO);
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(Enrollments))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<EnrollmentDTO>>> findById(@PathVariable("id") String id){
        return enrollmentService.findById(id)
                .map(this::convertToDTO)
                .map(Enrollment -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(Enrollment))
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<EnrollmentDTO>>> save(@Valid @RequestBody EnrollmentDTO EnrollmentDTO, final ServerHttpRequest request){
        return enrollmentService.save(this.convertToDocument(EnrollmentDTO))
                .map(this::convertToDTO)
                .map(co->ResponseEntity.created(
                                URI.create(request.getURI().toString().concat("/").concat(co.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(co))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<EnrollmentDTO>> update(@PathVariable("id") String id, @Valid @RequestBody EnrollmentDTO EnrollmentDTO){
        return Mono.just(convertToDocument(EnrollmentDTO)).map(
                        c -> {
                            c.setId(id);
                            return c;
                        })
                .flatMap(e ->enrollmentService.update(id,e))
                .map(this::convertToDTO)
                .map(e->ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id){
        return enrollmentService.delete(id)
                .flatMap(result -> {
                    if(result){
                        return Mono.just(ResponseEntity.noContent().build());
                    }else{
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }


    @GetMapping("/pageable")
    public Mono<ResponseEntity<PageSupport<EnrollmentDTO>>> getPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "2") int size
    ){
        return enrollmentService.getPage(PageRequest.of(page, size))
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

    private EnrollmentDTO EnrollmentDtoHateoas;

    @GetMapping("/hateoas/{id}")
    public Mono<EntityModel<EnrollmentDTO>> getHateos(@PathVariable("id") String id){
        Mono<Link> monoLink = linkTo(methodOn(EnrollmentController.class).findById(id)).withRel("Enrollment-info").toMono();

        // Common practice
        /* return EnrollmentService.findById(id)
                .map(this::convertToDTO)
                .flatMap(d->{
                    this.EnrollmentDtoHateoas = d;
                    return monoLink;
                })
                .map(link -> EntityModel.of(this.EnrollmentDtoHateoas,link)); */

        // Intermediate practice
        /* return EnrollmentService.findById(id)
                .map(this::convertToDTO)
                .flatMap(EnrollmentDTO -> monoLink.map(link->EntityModel.of(EnrollmentDTO,link))); */

        // Ideal practice
        return enrollmentService.findById(id)
                .map(this::convertToDTO)
                .zipWith(monoLink , EntityModel::of); //(EnrollmentDTO, link) -> EntityModel.of(EnrollmentDTO, link)
    }

    private EnrollmentDTO convertToDTO (Enrollment model){
        return  modelMapper.map(model,EnrollmentDTO.class);
    }

    private Enrollment convertToDocument (EnrollmentDTO dto){
        return  modelMapper.map(dto,Enrollment.class);
    }

}
