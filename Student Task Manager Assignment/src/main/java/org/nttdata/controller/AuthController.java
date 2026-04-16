package org.nttdata.controller;

import lombok.RequiredArgsConstructor;
import org.nttdata.dto.UserDTO;
import org.nttdata.dto.requests.AuthRequest;
import org.nttdata.service.JwtService;
import org.nttdata.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        Long id = userService.save(userDTO);
        UserDTO newUser = userService.getUserById(id);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("login")
    public String authenticateAndGenerateToken(@RequestBody AuthRequest authRequest){

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUsername());
        }else{
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

}
