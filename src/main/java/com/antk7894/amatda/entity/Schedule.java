package com.antk7894.amatda.entity;

import com.antk7894.amatda.entity.auditing.TimeTrackedEntity;
import com.antk7894.amatda.entity.planner.Planner;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @JoinColumn(name = "planner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Planner planner;

    private String title;

    private String description;

    private LocalDateTime eventDate;

    public Schedule(Planner planner, String title, String description, LocalDateTime eventDate) {
        this.planner = planner;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
    }

    public void updateSchedule(String title, String description, LocalDateTime eventDate) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
    }

}
