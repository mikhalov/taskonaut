package com.mikhalov.taskonaut.util;

import com.mikhalov.taskonaut.dto.telegram.BaseCallbackData;
import com.mikhalov.taskonaut.dto.telegram.TemporaryUserEntry;
import com.mikhalov.taskonaut.exception.TelegramCallbackNotFoundException;
import com.mikhalov.taskonaut.exception.TelegramConnectionTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TelegramBotUtil {

    private final ConcurrentHashMap<String, BaseCallbackData> callbackDataMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, TemporaryUserEntry> tokenMap = new ConcurrentHashMap<>();
    @Value("${telegram.token.expiration}")
    private long tokenDuration;

    public String generateDeepLinkToken(String userId) {
        String token = UUID.randomUUID().toString();
        TemporaryUserEntry temporaryUserEntry = new TemporaryUserEntry(Duration.ofMillis(tokenDuration), userId);
        tokenMap.put(token, temporaryUserEntry);
        return token;
    }

    public String getUserIdFromToken(String token) throws TelegramConnectionTokenException {
        TemporaryUserEntry userEntry = Optional.ofNullable(tokenMap.get(token))
                .orElseThrow(() -> new TelegramConnectionTokenException(
                        "Wrong command! Use only available in menu commands")
                );
        if (userEntry.isExpired()) {
            throw new TelegramConnectionTokenException("Connection token has been expired");
        } else {
            return userEntry.getUserId();
        }
    }

    public <T extends BaseCallbackData> String generateCallbackDataKey(T callbackData) {
        String uuid = UUID.randomUUID().toString();
        callbackDataMap.put(uuid, callbackData);

        return uuid;
    }

    @Scheduled(fixedRate = 600000)
    public void cleanUpExpiredTokens() {
        tokenMap.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    @Scheduled(fixedRate = 1000000)
    public void cleanUpExpiredCallbacks() {
        callbackDataMap.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public BaseCallbackData getCallbackDataValue(String callbackData) throws TelegramCallbackNotFoundException {
        return Optional.ofNullable(callbackDataMap.remove(callbackData))
                .orElseThrow(() -> new TelegramCallbackNotFoundException(
                        "Callback not found or expired\nTry again"));
    }
}
