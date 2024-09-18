package com.agutierrezl.easy_enrollment_api.service.impl;

import com.agutierrezl.easy_enrollment_api.model.Role;
import com.agutierrezl.easy_enrollment_api.model.User;
import com.agutierrezl.easy_enrollment_api.repository.IRoleRepository;
import com.agutierrezl.easy_enrollment_api.repository.IUserRepository;
import com.agutierrezl.easy_enrollment_api.repository.IGenericRepository;
import com.agutierrezl.easy_enrollment_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CRUDImpl<User,String> implements IUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    protected IGenericRepository<User, String> getRepository() {
        return userRepository;
    }

    @Override
    public Mono<com.agutierrezl.easy_enrollment_api.security.User> searchByUser(String username) {
        return userRepository.findOneByUsername(username)
                .flatMap(user -> Flux.fromIterable(user.getRoles())
                        .flatMap(userRole -> roleRepository.findById(userRole.getId())
                                .map(Role::getName))
                        .collectList()
                        .map(roles -> new com.agutierrezl.easy_enrollment_api.security.User(user.getUsername(), user.getPassword(), user.isStatus(), roles))
                );
    }
}
