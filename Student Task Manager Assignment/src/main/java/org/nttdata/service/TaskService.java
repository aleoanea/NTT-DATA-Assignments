package org.nttdata.service;

import lombok.RequiredArgsConstructor;
import org.nttdata.dto.TaskDto;
import org.nttdata.dto.UserMapper;
import org.nttdata.model.Task;
import org.nttdata.model.User;
import org.nttdata.repository.TaskRepository;
import org.nttdata.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream().map(task -> {return new TaskDto(task.getTaskId(), task.getTitle(), task.getDescription(), task.getCreatedAt(), UserMapper.mapUserToUserDTO(task.getOwner()));}).toList();
    }

    public List<TaskDto> getTasksByOwner(String owner) {
        return taskRepository.findByOwnerUsername(owner).stream().map(task -> {return new TaskDto(task.getTaskId(), task.getTitle(), task.getDescription(), task.getCreatedAt(), UserMapper.mapUserToUserDTO(task.getOwner()));}).toList();
    }

    public TaskDto saveTask(TaskDto taskDto, String username) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            Task task = Task.builder().title(taskDto.title()).description(taskDto.description())
                    .owner(user).createdAt(LocalDateTime.now()).build();
            var taskSalvat = taskRepository.save(task);
            return new TaskDto(taskSalvat.getTaskId(), taskSalvat.getTitle(), taskSalvat.getDescription(), taskSalvat.getCreatedAt(), UserMapper.mapUserToUserDTO(taskSalvat.getOwner()));
        }
        return null;

    }

    public TaskDto deleteTask(Long taskId) {
        var taskDeleted = taskRepository.findById(taskId);
        if (taskDeleted.isPresent()) {
            taskRepository.deleteById(taskDeleted.get().getTaskId());
            return new TaskDto(taskId, taskDeleted.get().getTitle(), taskDeleted.get().getDescription(), taskDeleted.get().getCreatedAt(), UserMapper.mapUserToUserDTO(taskDeleted.get().getOwner()));
        }
        return null;
    }

}
