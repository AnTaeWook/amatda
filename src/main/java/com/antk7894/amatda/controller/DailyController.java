package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.daily.request.DailyCreateDto;
import com.antk7894.amatda.dto.daily.request.DailyActionDto;
import com.antk7894.amatda.dto.daily.request.DailyUpdateDto;
import com.antk7894.amatda.dto.daily.response.DailyInquireDto;
import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.service.DailyService;
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
@RequestMapping("/api/daily")
public class DailyController {

    private final DailyService dailyService;

    @GetMapping
    public ResponseEntity<Page<DailyInquireDto>> all(Pageable pageable) {
        return ResponseEntity.ok(dailyService.findAll(pageable).map(DailyInquireDto::from));
    }

    @GetMapping("/{dailyId}")
    public ResponseEntity<DailyInquireDto> one(@PathVariable Long dailyId) {
        return ResponseEntity.ok(DailyInquireDto.from(dailyService.findOneById(dailyId)));
    }

    @PostMapping
    public ResponseEntity<DailyInquireDto> newDaily(@RequestBody @Valid DailyCreateDto dto) {
        Daily daily = dailyService.saveOne(dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        URI dailyUri = URI.create(currentUri + "/" + daily.getDailyId());
        return ResponseEntity.created(dailyUri).body(DailyInquireDto.from(daily));
    }

    @PutMapping("/{dailyId}")
    public ResponseEntity<DailyInquireDto> updateDaily(@PathVariable Long dailyId, @RequestBody @Valid DailyUpdateDto dto) {
        return ResponseEntity.ok(DailyInquireDto.from(dailyService.updateOne(dailyId, dto)));
    }

    @PatchMapping("/{dailyId}")
    public ResponseEntity<DailyInquireDto> manipulateDaily(@PathVariable Long dailyId, @RequestBody @Valid DailyActionDto dto) {
        return switch (dto.action()) {
            case FINISH -> ResponseEntity.ok(DailyInquireDto.from(dailyService.finishOne(dailyId)));
            case RESET -> ResponseEntity.ok(DailyInquireDto.from(dailyService.resetOne(dailyId)));
        };
    }

    @DeleteMapping("/{dailyId}")
    public ResponseEntity<?> deleteDaily(@PathVariable Long dailyId) {
        dailyService.removeOne(dailyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
