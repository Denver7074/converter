package com.tbot.ConverterValue.repositories;

import com.tbot.ConverterValue.enums.Length;
import com.tbot.ConverterValue.enums.Mass;
import com.tbot.ConverterValue.repositories.implement.HashMapModeService;

public interface ModeRep {

   static ModeRep getInstance(){return new HashMapModeService();}
   Mass getNowMass(Long chatId);
   Mass getFutureMass(Long chatId);
   void setNowMass(Long chatId,Mass mass);
   void setFutureMass(Long chatId,Mass mass);
   Length getNowLength(Long chatId);
   Length getFutureLength(Long chatId);
   void setNowLength(Long chatId,Length length);
   void setFutureLength(Long chatId,Length length);

}
