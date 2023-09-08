package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;

@Getter
public class NewMasterMessage extends ServerMessage{
   private Long masterId;

   public NewMasterMessage(Long masterId) {
      this();
      this.masterId = masterId;
   }

   public NewMasterMessage(){
      super(ServerMessageType.NEW_MASTER);
   }
}
