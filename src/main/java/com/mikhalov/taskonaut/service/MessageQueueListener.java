package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.dto.NoteForTelegramDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageQueueListener {

    private final NoteService noteService;
    private final TelegramBotService telegramBotService;

    @RabbitListener(queues = {"${spring.rabbitmq.note_to_telegram_queue}"})
    public void processNoteForTelegramBot(NoteForTelegramDTO noteForTelegramDTO) {
        log.trace("process message from ${spring.rabbitmq.note_to_telegram_queue}:" +
                        " noteId {} for user chatId {}",
                noteForTelegramDTO.noteId(),
                noteForTelegramDTO.chatId()
        );

        NoteDTO noteDTO = noteService.getNoteDTOByNoteForTelegramDTO(noteForTelegramDTO);
        telegramBotService.sendNoteToBot(noteForTelegramDTO.chatId(), noteDTO);
    }
}
