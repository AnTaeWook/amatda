package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.schedule.request.ScheduleCreateDto;
import com.antk7894.amatda.dto.schedule.request.ScheduleUpdateDto;
import com.antk7894.amatda.dto.schedule.response.ScheduleInquireDto;
import com.antk7894.amatda.entity.Schedule;
import com.antk7894.amatda.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<Page<ScheduleInquireDto>> all(Pageable pageable) {
        return ResponseEntity.ok(scheduleService.findAll(pageable).map(ScheduleInquireDto::from));
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleInquireDto> one(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(ScheduleInquireDto.from(scheduleService.findOneById(scheduleId)));
    }

    @PostMapping
    public ResponseEntity<ScheduleInquireDto> newSchedule(@RequestBody @Valid ScheduleCreateDto dto) {
        Schedule schedule = scheduleService.saveOne(dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        URI scheduleUri = URI.create(currentUri + "/" + schedule.getScheduleId());
        return ResponseEntity.created(scheduleUri).body(ScheduleInquireDto.from(schedule));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleInquireDto> updateSchedule(@PathVariable Long scheduleId, @RequestBody @Valid ScheduleUpdateDto dto) {
        return ResponseEntity.ok(ScheduleInquireDto.from(scheduleService.updateOne(scheduleId, dto)));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.removeOne(scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
