package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.jwt.TokenInfo;
import com.antk7894.amatda.dto.planner.PlannerJoinRequestDto;
import com.antk7894.amatda.dto.planner.PlannerLoginRequestDto;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService plannerService;

    @GetMapping
    public ResponseEntity<Page<Planner>> all(Pageable pageable) {
        return ResponseEntity.ok(plannerService.findAll(pageable));
    }

    @GetMapping("/{plannerId}")
    public ResponseEntity<Planner> one(@PathVariable Long plannerId) {
        return ResponseEntity.ok(plannerService.findOneById(plannerId));
    }

    @PostMapping
    public ResponseEntity<Planner> newPlanner(@RequestBody PlannerJoinRequestDto dto) {
        Planner planner = plannerService.saveOne(dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        return ResponseEntity.created(URI.create(currentUri + "/" + planner.getPlannerId())).body(planner);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody PlannerLoginRequestDto dto) {
        return ResponseEntity.ok(plannerService.login(dto));
    }

    @DeleteMapping("/{plannerId}")
    public ResponseEntity<?> deletePlanner(@PathVariable Long plannerId) {
        plannerService.removeOne(plannerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
