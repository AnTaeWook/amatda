package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.daily.DailyCreateRequestDto;
import com.antk7894.amatda.dto.daily.DailyPatchRequestDto;
import com.antk7894.amatda.dto.daily.DailyUpdateRequestDto;
import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.service.DailyService;
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
@RequestMapping("/api/daily")
public class DailyController {

    private final DailyService dailyService;

    @GetMapping
    public ResponseEntity<Page<Daily>> all(Pageable pageable) {
        return ResponseEntity.ok(dailyService.findAll(pageable));
    }

    @GetMapping("/{dailyId}")
    public ResponseEntity<Daily> one(@PathVariable Long dailyId) {
        return ResponseEntity.ok(dailyService.findOneById(dailyId));
    }

    @PostMapping
    public ResponseEntity<Daily> newDaily(@RequestBody DailyCreateRequestDto dto) {
        Daily daily = dailyService.saveOne(dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        return ResponseEntity.created(URI.create(currentUri + "/" + daily.getDailyId())).body(daily);
    }

    @PutMapping("/{dailyId}")
    public ResponseEntity<Daily> updateDaily(@PathVariable Long dailyId, @RequestBody DailyUpdateRequestDto dto) {
        return ResponseEntity.ok(dailyService.updateOne(dailyId, dto));
    }

    @PatchMapping("/{dailyId}")
    public ResponseEntity<Daily> manipulateDaily(@PathVariable Long dailyId, @RequestBody DailyPatchRequestDto dto) {
        return switch (dto.action()) {
            case FINISH -> ResponseEntity.ok(dailyService.finishOne(dailyId));
            case RESET -> ResponseEntity.ok(dailyService.resetOne(dailyId));
        };
    }

    @DeleteMapping("/{dailyId}")
    public ResponseEntity<?> deleteDaily(@PathVariable Long dailyId) {
        dailyService.removeOne(dailyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
