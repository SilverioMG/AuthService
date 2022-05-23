package net.atopecode.authservice.localization.messagelocalized.exceptions;

import net.atopecode.authservice.localization.messagelocalized.MessageLocalized;

public class RuntimeExceptionWithLocalizedMessage extends RuntimeException {

    private final MessageLocalized messageLocalized;

    public RuntimeExceptionWithLocalizedMessage(String messageLog, MessageLocalized messageLocalized){
        super(messageLog);
        this.messageLocalized = messageLocalized;
    }

    public MessageLocalized getMessageLocalized(){
        return messageLocalized;
    }
}
