package com.antk7894.amatda.entity;

import com.antk7894.amatda.entity.auditing.TimeTrackedEntity;
import com.antk7894.amatda.entity.planner.Planner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Daily extends TimeTrackedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyId;

    @JsonIgnore
    @JoinColumn(name = "planner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Planner planner;

    private String title;

    private String description;

    @Setter
    private boolean isFinished;

    public Daily(Planner planner, String title, String description, boolean isFinished) {
        this.planner = planner;
        this.title = title;
        this.description = description;
        this.isFinished = isFinished;
    }

    public void updateTitleAndDescription(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
