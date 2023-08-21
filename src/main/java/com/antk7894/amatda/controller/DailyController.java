package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.daily.DailyCreateRequestDto;
import com.antk7894.amatda.dto.daily.DailyPatchRequestDto;
import com.antk7894.amatda.dto.daily.DailyUpdateRequestDto;
import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.service.DailyReadService;
import com.antk7894.amatda.service.DailyService;
import com.antk7894.amatda.service.PlannerReadService;
import com.antk7894.amatda.util.CustomSecurityUtil;
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
@RequestMapping("/api/daily")
public class DailyController {

    private final DailyService dailyService;
    private final DailyReadService dailyReadService;
    private final PlannerReadService plannerReadService;

    @GetMapping
    public ResponseEntity<Page<Daily>> all(Pageable pageable) {
        Planner planner = plannerReadService.findOneByEmail(CustomSecurityUtil.getCurrentUserEmail());
        return ResponseEntity.ok(dailyReadService.findAll(planner, pageable));
    }

    @GetMapping("/{dailyId}")
    public ResponseEntity<Daily> one(@PathVariable Long dailyId) {
        Planner planner = plannerReadService.findOneByEmail(CustomSecurityUtil.getCurrentUserEmail());
        return ResponseEntity.ok(dailyReadService.findOneById(planner, dailyId));
    }

    @PostMapping
    public ResponseEntity<Daily> newDaily(@RequestBody DailyCreateRequestDto dto) {
        Planner planner = plannerReadService.findOneByEmail(CustomSecurityUtil.getCurrentUserEmail());
        Daily daily = dailyService.saveOne(planner, dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        return ResponseEntity.created(URI.create(currentUri + "/" + daily.getDailyId())).body(daily);
    }

    @PutMapping("/{dailyId}")
    public ResponseEntity<Daily> updateDaily(@PathVariable Long dailyId, @RequestBody DailyUpdateRequestDto dto) {
        Planner planner = plannerReadService.findOneByEmail(CustomSecurityUtil.getCurrentUserEmail());
        return ResponseEntity.ok(dailyService.updateOne(planner, dailyId, dto));
    }

    @PatchMapping("/{dailyId}")
    public ResponseEntity<Daily> manipulateDaily(@PathVariable Long dailyId, @RequestBody DailyPatchRequestDto dto) {
        Planner planner = plannerReadService.findOneByEmail(CustomSecurityUtil.getCurrentUserEmail());
        return switch (dto.action()) {
            case FINISH -> ResponseEntity.ok(dailyService.finishOne(planner, dailyId));
            case RESET -> ResponseEntity.ok(dailyService.resetOne(planner, dailyId));
        };
    }

    @DeleteMapping("/{dailyId}")
    public ResponseEntity<?> deleteDaily(@PathVariable Long dailyId) {
        Planner planner = plannerReadService.findOneByEmail(CustomSecurityUtil.getCurrentUserEmail());
        dailyService.removeOne(planner, dailyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
