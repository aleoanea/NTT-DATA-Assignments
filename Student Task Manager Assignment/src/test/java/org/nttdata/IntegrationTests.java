package org.nttdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nttdata.controller.AdminController;
import org.nttdata.controller.AuthController;
import org.nttdata.controller.TaskController;
import org.nttdata.dto.UserDTO;
import org.nttdata.model.Role;
import org.nttdata.repository.UserRepository;
import org.nttdata.service.AuthService;
import org.nttdata.service.JwtService;
import org.nttdata.service.TaskService;
import org.nttdata.service.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class IntegrationTests {

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService), new AuthController(userService, jwtService, authenticationManager ), new AdminController(userService, taskService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetTasksWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/tasks/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void testUserRoleCannotAccessAdminEndpoint() throws Exception {

        String userJwt = generateJwtWithUserRole();

        mockMvc.perform(get("/api/admin/tasks/all")
                        .header("Authorization", "Bearer " + userJwt)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private String generateJwtWithUserRole() {
        return Jwts.builder()
                .setSubject("user1")
                .claim("roles", List.of("ROLE_USER"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(
                        SignatureAlgorithm.HS256,
                        "5367566859703373367639792F423F452848284D6251655468576D5A71347437"
                )
                .compact();
    }



    @Test
    public void testRegisterWithoutAuthentication_shouldReturn200() throws Exception {

        UserDTO request = new UserDTO(
                1L,
                "newuser",
                "password123",
                Role.USER
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }


}