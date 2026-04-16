package org.nttdata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nttdata.dto.UserDTO;
import org.nttdata.dto.UserMapper;
import org.nttdata.model.Role;
import org.nttdata.model.User;
import org.nttdata.repository.UserRepository;
import org.nttdata.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void save_shouldEncodePassword_setUserRole_andReturnUserId() {
        UserDTO userDTO = new UserDTO(1L, "testuser", "plainPassword", Role.USER);

        User mappedUser = new User();
        mappedUser.setUsername("testuser");
        mappedUser.setPassword("plainPassword");

        User savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.USER);

        try (MockedStatic<UserMapper> mapperMock =
                     Mockito.mockStatic(UserMapper.class)) {

            mapperMock
                    .when(() -> UserMapper.mapUserDTOToUser(userDTO))
                    .thenReturn(mappedUser);

            when(passwordEncoder.encode("plainPassword"))
                    .thenReturn("encodedPassword");

            when(userRepository.save(any(User.class)))
                    .thenReturn(savedUser);

            Long result = userService.save(userDTO);

            assertEquals(1L, result);

            verify(passwordEncoder).encode("plainPassword");
            verify(userRepository).save(argThat(user ->
                    user.getPassword().equals("encodedPassword") &&
                            user.getRole() == Role.USER
            ));
        }
    }


    @Test
    void findAll_shouldReturnListOfUserDTOs() {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("user2");

        List<User> users = List.of(user1, user2);

        UserDTO dto1 = new UserDTO(1L , "user1", "pass", Role.USER);
        UserDTO dto2 = new UserDTO(2L, "user2", "pass", Role.USER);

        List<UserDTO> userDTOs = List.of(dto1, dto2);

        when(userRepository.findAll()).thenReturn(users);

        try (MockedStatic<UserMapper> mapperMock =
                     Mockito.mockStatic(UserMapper.class)) {

            mapperMock
                    .when(() -> UserMapper.mapUserListToUserDTOList(users))
                    .thenReturn(userDTOs);


            List<UserDTO> result = userService.findAll();


            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("user1", result.get(0).username());
            assertEquals("user2", result.get(1).username());

            verify(userRepository).findAll();
            }
        }


    @Test
    void delete_shouldDeleteUser_whenUserExists() {
        Long userId = 1L;

        User user = new User();
        user.setUserId(userId);
        user.setUsername("testuser");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }


    @Test
    void delete_shouldThrowException_whenUserDoesNotExist() {
        Long userId = 99L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception =
                assertThrows(NoSuchElementException.class,
                        () -> userService.delete(userId));

        assertEquals("user not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }


}
