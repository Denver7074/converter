package com.tbot.ConverterValue.services;

import com.tbot.ConverterValue.enums.Length;
import com.tbot.ConverterValue.enums.Mass;
import com.tbot.ConverterValue.repositories.ModeRep;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

@Service
@Slf4j
public class BotService {

    private final ModeRep modeRep = ModeRep.getInstance();

    public SendMessage sendMessage(Update update) {
        String command = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        Optional<Double> value = parseDouble(command);
        if (!value.isPresent()) {
            String firstName = update.getMessage().getChat().getFirstName();
            String answer = "Моя твоя не понимать";
            switch (command) {
                case "/start":
                    answer = "Привет, " + firstName + ", рад тебя видеть!";
                    return sendMessage(chatId, command, answer);
                case "/help":
                    answer = "Бот поможет вам конвертировать единицы измерения для массы или для длины.\n\n" +
                            "Вы можете выполнять команды из главного меню слева или нажимая на соответствующие кнопки клавиатуры:\n\n" +
                            "Введите /start, чтобы увидеть приветственное сообщение\n\n" +
                            "Введите /help, чтобы снова увидеть это сообщение";
                    return sendMessage(chatId, command, answer);
                case "Мера массы":
                    answer = "Выберите единицу измерения масс ТЕКУЩУЮ и БУДУЩУЮ:";
                    writeToFile(1);
                    return sendMessage(chatId, command, answer);
                case "Мера длины":
                    answer = "Выберите единицу измерения длины ТЕКУЩУЮ и БУДУЩУЮ:";
                    writeToFile(2);
                    return sendMessage(chatId, command, answer);
                default:
                    return sendMessage(chatId, command, answer);
            }

        }else {
                int number = readFile();
                Mass nowMass = modeRep.getNowMass(chatId);
                Mass futureMass = modeRep.getFutureMass(chatId);
                double now = nowMass.getValue();
                double future = futureMass.getValue();
            if (number == 2){
                    Length nowL = modeRep.getNowLength(chatId);
                    Length futureL = modeRep.getFutureLength(chatId);
                    now = nowL.getValue();
                    future = futureL.getValue();
            }
            double result = now * value.get() / future;
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(String.valueOf(result))
                    .build();
        }
    }
    @SneakyThrows
    private void writeToFile(Integer number){
        File file = new File("message");
        PrintWriter pr = new PrintWriter(file);
        pr.print(number);
        pr.close();
    }
    @SneakyThrows
    private int readFile(){
        File file = new File("message");
        Scanner scanner = new Scanner(file);
        String line = scanner.next();
        return Integer.parseInt(line);
    }
    private Optional<Double> parseDouble(String messageText) {
        try {
            return Optional.of(Double.parseDouble(messageText));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    //формирует меню бота
    public static SetMyCommands menu(){
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start","Начинаем"));
        botCommandList.add(new BotCommand("/help","Расскажи о себе"));
        return new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null);
    }

    //добавление экранной клавиатуры
    public EditMessageReplyMarkup handleCallback(Update update) {
        Message message = update.getCallbackQuery().getMessage();
        String[] param = update.getCallbackQuery().getData().split(":");
        String action = param[0];
        boolean c = false;
        for (Mass mass : Mass.values()){
            if (param[1].equalsIgnoreCase(String.valueOf(mass))){
                c = true;
            }
            else {
                continue;
            }
        }
        if (c == true) {
            Mass newMass = Mass.valueOf(param[1]);
            switch (action) {
                case "Now":
                    modeRep.setNowMass(message.getChatId(), newMass);
                    break;
                case "Future":
                    modeRep.setFutureMass(message.getChatId(), newMass);
                    break;
            }
            return EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId().toString())
                    .messageId(message.getMessageId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(addButtonsMass(message.getChatId())).build())
                    .build();
        }
            Length newLen = Length.valueOf(param[1]);
            switch (action) {
                case "NowL":
                    modeRep.setNowLength(message.getChatId(), newLen);
                    break;
                case "FutureL":
                    modeRep.setFutureLength(message.getChatId(), newLen);
                    break;
            }
            return EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId().toString())
                    .messageId(message.getMessageId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(addButtonsLength(message.getChatId())).build())
                    .build();
        }


    private SendMessage sendMessage(Long chatId,String command, String textToAnswer) {
        SendMessage message = new SendMessage();
        message.setText(textToAnswer);
        message.setChatId(chatId);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Мера массы");
        keyboardRows.add(row);
        row = new KeyboardRow();
        row.add("Мера длины");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        message.setReplyMarkup(keyboardMarkup);
        if (command.equals("Мера массы")){
            message.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(addButtonsMass(chatId)).build());
        }
        if (command.equals("Мера длины")) {
            message.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(addButtonsLength(chatId)).build());
        }
        return message;
    }
    private String addIconLength(Length save,Length current){
        return save == current ? current.getLustName() + "✅": current.getLustName();
    }
    private String addIconMass(Mass save,Mass current){
        return save == current ? current.getLustName() + "✅": current.getLustName();
    }
    private List<List<InlineKeyboardButton>> addButtonsMass(Long chatId) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Mass nowMass = modeRep.getNowMass(chatId);
        Mass futureMass = modeRep.getFutureMass(chatId);
        for (Mass mass : Mass.values()) {
            buttons.add(Arrays.asList(
                    InlineKeyboardButton.builder()
                            .text(addIconMass(nowMass, mass))
                            .callbackData("Now:" + mass)
                            .build(),
                    InlineKeyboardButton.builder()
                            .text(addIconMass(futureMass, mass))
                            .callbackData("Future:" + mass)
                            .build()));
        }
        return buttons;
    }
    private List<List<InlineKeyboardButton>> addButtonsLength(Long chatId) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        Length nowLength = modeRep.getNowLength(chatId);
        Length futureLength = modeRep.getFutureLength(chatId);
        for (Length length : Length.values()) {
            buttons.add(Arrays.asList(
                    InlineKeyboardButton.builder()
                            .text(addIconLength(nowLength,length))
                            .callbackData("NowL:" + length)
                            .build(),
                    InlineKeyboardButton.builder()
                            .text(addIconLength(futureLength,length))
                            .callbackData("FutureL:" + length)
                            .build()));
        }
        return buttons;
    }



}
