package com.agutierrezl.easy_enrollment_api.repository;

import com.agutierrezl.easy_enrollment_api.model.User;
import reactor.core.publisher.Mono;

public interface IUserRepository extends IGenericRepository<User, String> {

    Mono<User> findOneByUsername(String username);
}
