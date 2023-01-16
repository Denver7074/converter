package com.tbot.ConverterValue.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Length {
    CENTIMETRE(20,"Сантиметр",1),
    INCH(21,"Дюйм",2.54),
    VERST(22,"Верста",106680),
    ARSHIN(23,"Аршин",71.12);
    private final int id;
    private final String lustName;
    private final double value;
}
