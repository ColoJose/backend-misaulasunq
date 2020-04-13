package com.misaulasunq.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Commission {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;
    @OneToMany(orphanRemoval = true)
    private Set<Schedule> schedules;

    public Commission() {
    }
}
