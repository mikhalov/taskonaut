package com.mikhalov.taskonaut.quartz;

import com.mikhalov.taskonaut.service.MessageQueueService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class NoteReminderToTelegramJob implements Job {

    private MessageQueueService messageQueueService;

    @Autowired
    public void setMessageQueueService(MessageQueueService messageQueueService) {
        this.messageQueueService = messageQueueService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String noteId = jobDataMap.getString("noteId");
        long chatId = jobDataMap.getLong("chatId");

        messageQueueService.sendNoteToTelegramExchange(noteId, chatId);
    }
}