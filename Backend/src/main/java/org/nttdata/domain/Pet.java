package org.nttdata.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="pets")
@Getter @Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    @Column(name="ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME", nullable = false)
    @NonNull
    private String name;

    @Column(name="OWNER", nullable = false)
    @NonNull
    private String owner;

    @Column(name="TYPE", nullable = false)
    @NonNull
    private String type;

    @Column(name="RACE", nullable = false)
    @NonNull
    private String race;

    @Column(name="REALAGE", nullable = false)
    @NonNull
    private Integer realAge;

    @Column(name="HUMANAGE", nullable = false)
    @NonNull
    private Integer humanAge;
}
