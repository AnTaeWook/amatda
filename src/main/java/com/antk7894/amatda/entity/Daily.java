package com.antk7894.amatda.entity;

import com.antk7894.amatda.entity.auditing.TimeTrackedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Daily extends TimeTrackedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyId;

    @JoinColumn(name = "planner_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Planner planner;

    private String title;

    private String description;

    private boolean isFinished;

    public Daily(Planner planner, String title, String description, boolean isFinished) {
        this.planner = planner;
        this.title = title;
        this.description = description;
        this.isFinished = isFinished;
    }

}
