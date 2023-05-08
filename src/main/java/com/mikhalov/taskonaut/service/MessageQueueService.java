package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.NoteForTelegramDTO;
import com.mikhalov.taskonaut.dto.ReminderDTO;
import com.mikhalov.taskonaut.exception.ScheduleJobException;
import com.mikhalov.taskonaut.quartz.NoteReminderToTelegramJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageQueueService {

    private final RabbitTemplate rabbitTemplate;
    private final Scheduler scheduler;
    @Value("${spring.rabbitmq.telegram_exchange}")
    private String telegramExchange;
    @Value("${spring.rabbitmq.note_routing_key}")
    private String noteRoutingKey;

    public void sendNoteToTelegramExchange(String noteId, Long chatId) {
        rabbitTemplate.convertAndSend(
                telegramExchange,
                noteRoutingKey,
                new NoteForTelegramDTO(noteId, chatId)
        );
    }

    public void setReminderForTelegram(ReminderDTO reminderDTO, long chatId) throws ScheduleJobException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("noteId", reminderDTO.noteId());
        jobDataMap.put("chatId", chatId);

        JobDetail jobDetail = JobBuilder.newJob(NoteReminderToTelegramJob.class)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(Date.from(reminderDTO.reminderDateTime().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new ScheduleJobException(e);
        }
    }

}
