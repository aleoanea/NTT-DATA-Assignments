package org.nttdata.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name="tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private LocalDateTime createdAt;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User owner;
}
