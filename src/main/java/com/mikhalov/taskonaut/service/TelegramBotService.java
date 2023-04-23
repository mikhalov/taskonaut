package com.mikhalov.taskonaut.service;

import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import com.mikhalov.taskonaut.exception.ExecuteNoteMessageException;
import com.mikhalov.taskonaut.exception.TelegramAccountAlreadyConnected;
import com.mikhalov.taskonaut.exception.TelegramBotHasNotConnectedException;
import com.mikhalov.taskonaut.exception.TelegramConnectionTokenException;
import com.mikhalov.taskonaut.model.User;
import com.mikhalov.taskonaut.util.UUIDTokenUtil;
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
    private final UUIDTokenUtil tokenUtil;
    @Value("${telegram.bot.username}")
    private String botUsername;

    public TelegramBotService(@Value("${telegram.bot.token}") String botToken,
                              @Autowired UserService userService,
                              @Autowired LabelService labelService,
                              @Autowired UUIDTokenUtil tokenUtil,
                              @Autowired NoteService noteService) {
        super(botToken);
        this.userService = userService;
        this.labelService = labelService;
        this.noteService = noteService;
        this.tokenUtil = tokenUtil;
        initCommands();

    }

    private void initCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/all_labels", "get all available labels"));
        listOfCommands.add(new BotCommand("/unlink", "cancel current connection with Taskonaut"));
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
            Long chatId = update.getMessage().getChatId();
            String inputText = update.getMessage().getText();
            if (inputText.startsWith("/start")) {
                executeTokenAndSaveChatIdToUser(chatId, inputText);
            } else if (isCurrentBotUserHasNotAlreadyAuth(chatId)) {
                sendMessage(chatId, "You need to connect this bot with your Taskonaut account");
            } else {
                executeMessage(update);
            }
        } else if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (isCurrentBotUserHasNotAlreadyAuth(chatId)) {
                sendMessage(chatId, "You need to connect this bot with your Taskonaut account");
            } else {
                executeCallback(update);
            }
        }
    }

    private void executeMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();
        log.trace("chat id '{}', message: {}", chatId, inputText);
        switch (inputText) {
            case "/all_labels" -> sendLabelsList(chatId);
            case "/unlink" -> unlinkConnection(chatId);
            default -> sendWelcomeMessage(chatId);
        }
    }


    private void unlinkConnection(Long chatId) {
        userService.removeChatIdFromUser(chatId);
        String successfulUnlinked = "Account has been unlinked successful";

        sendMessage(chatId, successfulUnlinked);
    }

    private void executeCallback(Update update) {
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

    private void executeTokenAndSaveChatIdToUser(Long chatId, String inputText) {
        String token = inputText.replace("/start ", "");
        try {
            String userId = tokenUtil.getUserIdFromToken(token);
            userService.setTelegramChatIdByUserId(chatId, userId);
            String successful = "Connected with Tasconaut app. \nNow you can use me";

            sendMessage(chatId, successful);
        } catch (TelegramAccountAlreadyConnected e) {
            String alreadyConnected = """
                    Your telegram account already connected with Taskanaut profile.
                    Use /unlink to cancel this connection""";
            sendMessage(chatId, alreadyConnected);
        } catch (TelegramConnectionTokenException e) {
            log.error(e.getMessage(), e);
            sendMessage(chatId, e.getMessage());
        }
    }

    private boolean isCurrentBotUserHasNotAlreadyAuth(long chatId) {
        log.info("is auth");

        return !userService.isChatIdRegisteredByUser(chatId);
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
        String welcome = "Welcome to the Taskonaut bot!";

        sendMessage(chatId, welcome);
    }

    private void sendMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("error while sending welcome message, user chat id '{}'", chatId, e);
        }
    }


    public void findAndSendNoteToUserById(String noteId) throws TelegramBotHasNotConnectedException, ExecuteNoteMessageException {
        Long chatId = userService.getCurrentUserTelegramChatId()
                .orElseThrow(TelegramBotHasNotConnectedException::new);
        NoteDTO noteDTO = noteService.getNoteDTOById(noteId);

        sendNoteToUser(chatId, noteDTO);
    }

    private void sendNoteToUser(Long chatId, NoteDTO noteDTO) throws ExecuteNoteMessageException {
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

    public String getTokenToConnectUserWithBot() {
        User user = userService.getCurrentUser();

        return tokenUtil.generateDeepLinkToken(user.getId());
    }
}