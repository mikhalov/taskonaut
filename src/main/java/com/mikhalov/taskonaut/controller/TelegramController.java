package com.mikhalov.taskonaut.controller;


import com.mikhalov.taskonaut.exception.ExecuteNoteMessageException;
import com.mikhalov.taskonaut.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramBotService telegramBotService;

    @GetMapping("/send-note/{id}")
    public ResponseEntity<String> sendNote(@PathVariable String id) {
        try {
            telegramBotService.findAndSendNoteToUserById(id);
            return ResponseEntity.ok("Note sent successfully");
        } catch (ExecuteNoteMessageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending note: " + e.getMessage());
        }
    }
}