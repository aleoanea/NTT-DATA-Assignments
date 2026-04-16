package org.nttdata.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.nttdata.model.Role;

@Builder
public record UserDTO (
        Long userId,
        String username,
        String password,
        Role role
){
}
