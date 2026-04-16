package org.nttdata.util;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.nttdata.model.Role;
import org.nttdata.model.User;
import org.nttdata.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String @NonNull ... args){
        createAdmin();
    }

    private void createAdmin(){
        if (userRepository.findByUsername("admin").isEmpty()){
            User user =new User();
            user.setUsername("admin");
            user.setPassword(Objects.requireNonNull(passwordEncoder.encode("admin")));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
            System.out.println("Admin created successfully");
        }

    }
}