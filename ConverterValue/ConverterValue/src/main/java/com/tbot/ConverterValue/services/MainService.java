package com.tbot.ConverterValue.services;

import com.tbot.ConverterValue.configurations.BotConfig;
import com.tbot.ConverterValue.enums.Mass;
import com.tbot.ConverterValue.repositories.ModeRep;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainService extends TelegramLongPollingBot {
    final BotConfig botConfig;
    BotService botService;

    @SneakyThrows
    public MainService(BotConfig botConfig, BotService botService)  {
        this.botConfig = botConfig;
        this.botService = botService;
        execute(BotService.menu());
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            execute(botService.handleCallback(update));
        }
        if (update.hasMessage() && update.getMessage().hasText()){
           execute(botService.sendMessage(update));
        }
    }




}





