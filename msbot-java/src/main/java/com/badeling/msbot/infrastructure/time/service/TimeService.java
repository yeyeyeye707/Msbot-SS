package com.badeling.msbot.infrastructure.time.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor(onConstructor = @___(@Autowired))
public class TimeService {
    private static final DateTimeFormatter formatter_8 = DateTimeFormatter.ofPattern("yyyyMMdd");

    public LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }

    public LocalDateTime getToday() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }


    /**
     * yyyyMMdd
     *
     * @return
     */
    public String getCurrentTimeString_8() {
        return formatter_8.format(getCurrentDate());
    }
}
