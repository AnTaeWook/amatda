package com.antk7894.amatda.entity.planner;

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

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    public Planner(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
