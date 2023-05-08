package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteForTelegramDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageQueueService {

    private final RabbitTemplate rabbitTemplate;

    public void sendNoteToTelegramExchange(String noteId, Long chatId) {
        rabbitTemplate.convertAndSend(
                "telegram-exchange",
                "notes",
                new NoteForTelegramDTO(noteId, chatId)
        );
    }

}
