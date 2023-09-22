package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.jwt.TokenInfo;
import com.antk7894.amatda.dto.planner.request.PlannerJoinDto;
import com.antk7894.amatda.dto.planner.request.PlannerLoginDto;
import com.antk7894.amatda.dto.planner.response.PlannerInquireDto;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.service.PlannerService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Page<PlannerInquireDto>> all(Pageable pageable) {
        return ResponseEntity.ok(plannerService.findAll(pageable).map(PlannerInquireDto::from));
    }

    @GetMapping("/{plannerId}")
    public ResponseEntity<PlannerInquireDto> one(@PathVariable Long plannerId) {
        return ResponseEntity.ok(PlannerInquireDto.from(plannerService.findOneById(plannerId)));
    }

    @PostMapping
    public ResponseEntity<PlannerInquireDto> newPlanner(@RequestBody @Valid PlannerJoinDto dto) {
        Planner planner = plannerService.saveOne(dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        URI plannerUri = URI.create(currentUri + "/" + planner.getPlannerId());
        return ResponseEntity.created(plannerUri).body(PlannerInquireDto.from(planner));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody @Valid PlannerLoginDto dto) {
        return ResponseEntity.ok(plannerService.login(dto));
    }

    @DeleteMapping("/{plannerId}")
    public ResponseEntity<?> deletePlanner(@PathVariable Long plannerId) {
        plannerService.removeOne(plannerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
