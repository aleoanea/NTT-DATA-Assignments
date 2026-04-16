package org.nttdata.service;

import lombok.RequiredArgsConstructor;
import org.nttdata.dto.UserRegistrationDTO;
import org.nttdata.model.Role;
import org.nttdata.model.User;
import org.nttdata.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void registerUser(UserRegistrationDTO registrationDTO){
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent())
            throw new RuntimeException("Username already exists");
        User user= User.builder()
                .username(registrationDTO.getUsername())
                .password(Objects.requireNonNull(passwordEncoder.encode(registrationDTO.getPassword())))
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }
}
