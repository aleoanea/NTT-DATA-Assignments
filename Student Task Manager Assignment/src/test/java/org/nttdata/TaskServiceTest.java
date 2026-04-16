package org.nttdata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nttdata.dto.TaskDto;
import org.nttdata.dto.UserDTO;
import org.nttdata.dto.UserMapper;
import org.nttdata.model.Role;
import org.nttdata.model.Task;
import org.nttdata.model.User;
import org.nttdata.repository.TaskRepository;
import org.nttdata.repository.UserRepository;
import org.nttdata.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAllTasks_shouldReturnAllTasksAsDtos() {

        User owner = new User();
        owner.setUserId(1L);
        owner.setUsername("alex");

        Task task1 = Task.builder()
                .taskId(1L)
                .title("Task 1")
                .description("Desc 1")
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .build();

        Task task2 = Task.builder()
                .taskId(2L)
                .title("Task 2")
                .description("Desc 2")
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .build();

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        try (MockedStatic<UserMapper> mapperMock =
                     Mockito.mockStatic(UserMapper.class)) {

            mapperMock.when(() -> UserMapper.mapUserToUserDTO(owner))
                    .thenReturn(new UserDTO(1L, "alex", "123", Role.USER));

            List<TaskDto> result = taskService.getAllTasks();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Task 1", result.get(0).title());
            assertEquals("alex", result.get(0).owner().username());

            verify(taskRepository).findAll();
        }
    }

    @Test
    void saveTask_shouldSaveAndReturnTaskDto_whenUserExists() {
        // Arrange
        String username = "alex";

        User user = new User();
        user.setUserId(1L);
        user.setUsername(username);

        TaskDto inputDto = new TaskDto(
                null,
                "New Task",
                "Task description",
                null,
                null
        );

        Task savedTask = Task.builder()
                .taskId(10L)
                .title("New Task")
                .description("Task description")
                .createdAt(LocalDateTime.now())
                .owner(user)
                .build();

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        try (MockedStatic<UserMapper> mapperMock =
                     Mockito.mockStatic(UserMapper.class)) {

            mapperMock.when(() -> UserMapper.mapUserToUserDTO(user))
                    .thenReturn(new UserDTO(1L, username, "",  Role.USER));

            TaskDto result = taskService.saveTask(inputDto, username);// Assert
            assertNotNull(result);
            assertEquals("New Task", result.title());
            assertEquals(username, result.owner().username());

            verify(taskRepository).save(any(Task.class));
        }
    }

    @Test
    void saveTask_shouldReturnNull_whenUserDoesNotExist() {

        when(userRepository.findByUsername("ghost"))
                .thenReturn(Optional.empty());

        TaskDto dto = new TaskDto(null, "Task", "Desc", null, null);

        TaskDto result = taskService.saveTask(dto, "ghost");

        assertNull(result);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_shouldDeleteAndReturnTaskDto_whenTaskExists() {
        User owner = new User();
        owner.setUsername("alex");

        Task task = Task.builder()
                .taskId(5L)
                .title("Important Task")
                .description("Desc")
                .createdAt(LocalDateTime.now())
                .owner(owner)
                .build();

        when(taskRepository.findById(5L))
                .thenReturn(Optional.of(task));

        try (MockedStatic<UserMapper> mapperMock =
                     Mockito.mockStatic(UserMapper.class)) {

            mapperMock.when(() -> UserMapper.mapUserToUserDTO(owner))
                    .thenReturn(new UserDTO(1L, "alex", "", Role.USER));

            TaskDto result = taskService.deleteTask(5L);

            assertNotNull(result);
            assertEquals("Important Task", result.title());

            verify(taskRepository).deleteById(5L);
        }
    }

    @Test
    void deleteTask_shouldReturnNull_whenTaskDoesNotExist() {
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        TaskDto result = taskService.deleteTask(99L);

        assertNull(result);
        verify(taskRepository, never()).deleteById(any());
    }
}
