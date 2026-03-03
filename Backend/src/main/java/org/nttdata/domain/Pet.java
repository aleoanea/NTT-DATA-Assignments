package org.nttdata.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="pets")
@Getter @Setter
public class Pet {

    @Id
    @Column(name="ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="OWNER", nullable = false)
    private String owner;

    @Column(name="TYPE", nullable = false)
    private String type;

    @Column(name="RACE", nullable = false)
    private String race;

    @Column(name="REALAGE", nullable = false)
    private Integer realAge;

    @Column(name="HUMANAGE", nullable = false)
    private Integer humanAge;
}
