package com.antk7894.amatda.entity;

import com.antk7894.amatda.entity.auditing.TimeTrackedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends TimeTrackedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @JoinColumn(name = "planner_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Planner planner;

    private String title;

    private String description;

    private LocalDateTime ddatetime;

    public Schedule(Planner planner, String title, String description, LocalDateTime ddatetime) {
        this.planner = planner;
        this.title = title;
        this.description = description;
        this.ddatetime = ddatetime;
    }

}
