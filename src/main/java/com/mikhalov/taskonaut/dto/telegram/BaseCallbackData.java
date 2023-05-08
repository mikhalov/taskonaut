package com.mikhalov.taskonaut.dto.telegram;

import java.time.Duration;
import java.time.Instant;

public interface BaseCallbackData {

    Instant CREATION_TIME = Instant.now();
    Duration EXPIRATION_DURATION = Duration.ofHours(5);

    default boolean isExpired() {
        return Instant.now().isAfter(CREATION_TIME.plus(EXPIRATION_DURATION));
    }

}
