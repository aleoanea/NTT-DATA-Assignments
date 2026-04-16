package org.nttdata.dto;

import org.nttdata.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskDto(Long taskId, String title, String description, LocalDateTime createdAt, UserDTO owner) {
}
