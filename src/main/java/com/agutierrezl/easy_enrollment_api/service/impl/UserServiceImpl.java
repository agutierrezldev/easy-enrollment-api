package com.agutierrezl.easy_enrollment_api.service.impl;

import com.agutierrezl.easy_enrollment_api.model.User;
import com.agutierrezl.easy_enrollment_api.repository.IUserRepository;
import com.agutierrezl.easy_enrollment_api.repository.IGenericRepository;
import com.agutierrezl.easy_enrollment_api.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CRUDImpl<User,String> implements IUserService {

    private final IUserRepository UserRepository;

    @Override
    protected IGenericRepository<User, String> getRepository() {
        return UserRepository;
    }

}
