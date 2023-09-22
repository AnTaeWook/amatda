package com.antk7894.amatda.dto.schedule.response;

import com.antk7894.amatda.entity.Schedule;

import java.time.LocalDateTime;

public record ScheduleInquireDto(
        Long scheduleId,
        String title,
        String description,
        LocalDateTime eventDate
) {

    public static ScheduleInquireDto from(Schedule schedule) {
        return new ScheduleInquireDto(
                schedule.getScheduleId(), schedule.getTitle(), schedule.getDescription(), schedule.getEventDate()
        );
    }

}
