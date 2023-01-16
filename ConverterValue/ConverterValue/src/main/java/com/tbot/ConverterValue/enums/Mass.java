package com.tbot.ConverterValue.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Mass {
    KILOGRAM(10,"Килограмм",1),
    CAD(11,"Кадь",229.32),
    QUARTER(12,"Четверть",49.14),
    PUD(13,"Пуд",16.38),
    POUND(14,"Фунт",0.41);
    private final int id;
    private final String lustName;
    private final double value;
}
