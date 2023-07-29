package com.antk7894.amatda.entity;

import com.antk7894.amatda.entity.auditing.TimeTrackedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Planner extends TimeTrackedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plannerId;

    @Column(unique = true)
    private String email;

    private String password;

    public Planner(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
