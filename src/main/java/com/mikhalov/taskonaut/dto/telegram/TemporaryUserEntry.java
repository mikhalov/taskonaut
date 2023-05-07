package com.mikhalov.taskonaut.dto.telegram;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
public class TemporaryUserEntry {


    private final Instant creationTime = Instant.now();
    private final  Duration expirationDuration;
    private final String userId;

    public String getUserId() {
        return userId;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(creationTime.plus(expirationDuration));
    }
}