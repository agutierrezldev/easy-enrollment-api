package com.agutierrezl.easy_enrollment_api.service;

import com.agutierrezl.easy_enrollment_api.model.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User, String> {

    Mono<com.agutierrezl.easy_enrollment_api.security.User> searchByUser(String username);
}
