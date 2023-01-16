package com.tbot.ConverterValue.repositories.implement;

import com.tbot.ConverterValue.enums.Length;
import com.tbot.ConverterValue.enums.Mass;
import com.tbot.ConverterValue.repositories.ModeRep;

import java.util.HashMap;
import java.util.Map;

public class HashMapModeService implements ModeRep {
    private final Map<Long,Mass> nowMass = new HashMap<>();
    private final Map<Long,Mass> futureMass = new HashMap<>();
    private final Map<Long,Length> nowLength = new HashMap<>();
    private final Map<Long,Length> futureLength = new HashMap<>();

    @Override
    public Mass getNowMass(Long chatId) {
        return nowMass.getOrDefault(chatId, Mass.KILOGRAM);
    }
    @Override
    public Mass getFutureMass(Long chatId) {
        return futureMass.getOrDefault(chatId, Mass.KILOGRAM);
    }
    @Override
    public void setNowMass(Long chatId, Mass mass) {
        nowMass.put(chatId,mass);
    }
    @Override
    public void setFutureMass(Long chatId, Mass mass) {
        futureMass.put(chatId,mass);
    }
    @Override
    public Length getNowLength(Long chatId) {
        return nowLength.getOrDefault(chatId,Length.CENTIMETRE);
    }
    @Override
    public Length getFutureLength(Long chatId) {
        return futureLength.getOrDefault(chatId,Length.CENTIMETRE);
    }
    @Override
    public void setNowLength(Long chatId, Length length) {
        nowLength.put(chatId,length);
    }
    @Override
    public void setFutureLength(Long chatId, Length length) {
    futureLength.put(chatId, length);
    }
}
