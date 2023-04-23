package com.mikhalov.taskonaut.controller;


import com.mikhalov.taskonaut.exception.ExecuteNoteMessageException;
import com.mikhalov.taskonaut.exception.TelegramBotHasNotConnectedException;
import com.mikhalov.taskonaut.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramBotService telegramBotService;
    @Value("${telegram.bot.username}")
    private String botUsername;

    @GetMapping("/send-note/{id}")
    public ResponseEntity<String> sendNote(@PathVariable String id) {
        try {
            telegramBotService.findAndSendNoteToUserById(id);
            return ResponseEntity.ok("Note sent successfully");
        } catch (TelegramBotHasNotConnectedException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Need to connect telegram bot");
        } catch (ExecuteNoteMessageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending note: " + e.getMessage());
        }
    }

    @PostMapping("/get-deep-link")
    public ResponseEntity<String> getDeepLink() {
        String token = telegramBotService.getTokenToConnectUserWithBot();
        String deepLink = String.format("https://t.me/%s?start=%s", botUsername, token);

        return ResponseEntity.ok(deepLink);
    }

}