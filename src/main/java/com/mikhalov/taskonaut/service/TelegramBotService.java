package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.exception.ExecuteNoteMessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {

    private static final String LABELS_MENU = "labelId_";
    private static final String TITLES_MENU = "noteTitle";

    private final UserService userService;
    private final LabelService labelService;
    private final NoteService noteService;
    @Value("${telegram.bot.username}")
    private String botUsername;

    public TelegramBotService(@Value("${telegram.bot.token}") String botToken,
                              @Autowired UserService userService,
                              @Autowired LabelService labelService,
                              @Autowired NoteService noteService) {
        super(botToken);
        this.userService = userService;
        this.labelService = labelService;
        this.noteService = noteService;
        initCommands();

    }

    private void initCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/all_labels", "get all available labels"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String inputText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            switch (inputText) {
                case "/start" -> sendWelcomeMessage(chatId);
                case "/all_labels" -> sendLabelsList(chatId);

                default -> sendWelcomeMessage(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (callbackData.startsWith(LABELS_MENU)) {
                String labelId = callbackData.substring(LABELS_MENU.length());
                sendNotesByLabelId(chatId, labelId);
            } else if (callbackData.startsWith(TITLES_MENU)) {
                String[] split = callbackData.split(";");
                String noteTitle = split[2];
                String labelId = split[1];
                sendNotesByTitleAndLabelId(chatId, noteTitle, labelId);
            }

        }
    }

    private void sendNotesByTitleAndLabelId(long chatId, String noteTitle, String labelId) {
        List<NoteDTO> notes = noteService.getNotesByTitleAndLabelId(chatId, noteTitle, labelId);

        notes.forEach(note -> sendNoteToUser(chatId, note));


    }

    private void sendNotesByLabelId(long chatId, String labelId) {
        List<String> noteUniqueTitles = noteService.getAllTitlesByLabelId(chatId, labelId);
        InlineKeyboardMarkup markup = createInlineKeyboardForNoteTitles(noteUniqueTitles, labelId);

        sendInlineKeyboardMessage(chatId, "Get all notes by title:", markup);
    }

    private InlineKeyboardMarkup createInlineKeyboardForNoteTitles(List<String> noteTitles, String labelId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(noteTitles.stream()
                .map(title -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(title);
                    button.setCallbackData(TITLES_MENU + ";" + labelId + ";" + title);

                    return button;
                })
                .map(List::of)
                .toList());
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

    private void sendInlineKeyboardMessage(long chatId, String textMessage, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textMessage);
        message.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private InlineKeyboardMarkup createInlineKeyboardForLabels(List<LabelDTO> labels) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(labels.stream()
                .map(label -> {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(label.getName());
                    button.setCallbackData(LABELS_MENU + label.getId());

                    return button;
                })
                .map(List::of)
                .toList());
        inlineKeyboardMarkup.setKeyboard(keyboard);

        return inlineKeyboardMarkup;
    }

    private void sendLabelsList(Long chatId) {
        List<LabelDTO> labels = labelService.getAllLabelsByChatId(chatId);
        InlineKeyboardMarkup markup = createInlineKeyboardForLabels(labels);
        String message = "select a label";
        sendInlineKeyboardMessage(chatId, message, markup);


    }

    private void sendWelcomeMessage(long chatId) {
        SendMessage welcomeMessage = new SendMessage();
        welcomeMessage.setChatId(chatId);
        welcomeMessage.setText("Welcome to the Taskonaut bot!");
        try {
            execute(welcomeMessage);
        } catch (TelegramApiException e) {
            log.error("error while sending welcome message, user chat id '{}'", chatId, e);
        }
    }


    public void findAndSendNoteToUserById(String noteId) {
        Long chatId = userService.getCurrentUserTelegramChatId();
        NoteDTO noteDTO = noteService.getNoteDTOById(noteId);

        sendNoteToUser(chatId, noteDTO);
    }

    private void sendNoteToUser(Long chatId, NoteDTO noteDTO) {
        String note = formatNoteForSending(noteDTO);
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(note);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("error when executing note '{}' to user '{}'", noteDTO.getId(), chatId);
            throw new ExecuteNoteMessageException(e);
        }
    }

    private String formatNoteForSending(NoteDTO noteDTO) {
        return Optional.of(noteDTO)
                .map(note -> String.format("%s%n%n%s%n%n%s%n%s",
                        note.getTitle(),
                        note.getContent(),
                        "Label: " + Optional.ofNullable(note.getLabelDTO())
                                .map(LabelDTO::getName)
                                .orElse(""),
                        note.getLastModifiedAt()))
                .orElseThrow();
    }


    private void saveChatId(long chatId) {
        userService.setCurrentUserTelegramChatId(chatId);
    }
}