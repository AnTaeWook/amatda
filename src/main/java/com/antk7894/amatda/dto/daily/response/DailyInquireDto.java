package com.antk7894.amatda.dto.daily.response;

import com.antk7894.amatda.entity.Daily;

public record DailyInquireDto(
        Long dailyId,
        String title,
        String description,
        boolean isFinished
) {

    public static DailyInquireDto from(Daily daily) {
        return new DailyInquireDto(
                daily.getDailyId(), daily.getTitle(), daily.getTitle(), daily.isFinished()
        );
    }

}
