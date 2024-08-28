package com.agutierrezl.matricula_facil_api.service.impl;

import com.agutierrezl.matricula_facil_api.repository.IGenericRepository;
import com.agutierrezl.matricula_facil_api.service.ICRUD;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CRUDImpl<T,ID> implements ICRUD<T,ID> {

    protected abstract IGenericRepository<T,ID> getRepository();

    @Override
    public Mono<T> save(T t) {
        return getRepository().save(t);
    }

    @Override
    public Mono<T> update(ID id, T t) {
        return getRepository().findById(id).flatMap(e->save(t));
    }

    @Override
    public Flux<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        return getRepository().findById(id)
                .hasElement().flatMap(result -> {
                    if(result){
                        return getRepository().deleteById(id).thenReturn(true);
                    }else{
                        return Mono.just(false);
                    }
                });
    }
}
