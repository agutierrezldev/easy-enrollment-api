package com.agutierrezl.easy_enrollment_api.initializer;

import com.agutierrezl.easy_enrollment_api.model.Role;
import com.agutierrezl.easy_enrollment_api.model.User;
import com.agutierrezl.easy_enrollment_api.repository.IRoleRepository;
import com.agutierrezl.easy_enrollment_api.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;


    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = new ArrayList<>();
        roleRepository.save(new Role(null, "ADMIN"))
                .map(role -> {
                    roles.add(role);
                    return roles;
                })
                .flatMap(rolesList -> {
                    User user = new User(null,
                            "agutierrezl",
                            "$2a$12$jRXC8njeM/jX1AQq7MvmReOTelYkD8gHn2AEKE7YXtrJBxLE0AkwW", //123
                            true,
                            rolesList);
                    return userRepository.save(user);
                })
                .subscribe(savedUser -> {
                    System.out.println("User: " + savedUser);
                });
    }
}
