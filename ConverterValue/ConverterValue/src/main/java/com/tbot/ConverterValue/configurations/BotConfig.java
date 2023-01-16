package com.tbot.ConverterValue.configurations;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@PropertySource("application.properties")
public class BotConfig {
    @Value("${bot.name}")
    String name;
    @Value("${bot.token}")
    String token;
}
