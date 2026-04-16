package org.nttdata.controller;

import lombok.RequiredArgsConstructor;
import org.nttdata.dto.TaskDto;
import org.nttdata.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("my")
    public ResponseEntity<List<TaskDto>> getMyTasks(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = authentication.getName();
        return ResponseEntity.ok(taskService.getTasksByOwner(username));
    }


    @PostMapping
    public ResponseEntity<TaskDto> saveTasks(@RequestBody TaskDto task, Authentication authentication) {
        return ResponseEntity.ok(taskService.saveTask(task, authentication.getName()));
    }
}
