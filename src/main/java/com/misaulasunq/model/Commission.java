package com.misaulasunq.model;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

public class Commission {

    @Id
    private String id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;
    @OneToMany(orphanRemoval = true)
    private Set<Schedule> schedules;
}
