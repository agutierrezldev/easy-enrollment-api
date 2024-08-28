package com.agutierrezl.matricula_facil_api.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUD<T,ID> {

    Mono<T> save(T t);
    Mono<T> update(ID id, T t);
    Flux<T> findAll();
    Mono<T> findById(ID id);
    Mono<Boolean> delete(ID id);

}
