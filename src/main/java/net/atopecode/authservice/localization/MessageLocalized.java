package net.atopecode.authservice.localization;

import java.io.Serializable;
import java.util.Arrays;

public class MessageLocalized implements Serializable {
	
	private static final long serialVersionUID = 8225920375041121608L;

	private final String messageCode;
	
	private final Object[] messageParams;
	
	public MessageLocalized() {
		this.messageCode = null;
		this.messageParams = new Object[0];
	}
	
	public MessageLocalized(String messageCode) {
		this(messageCode, new Object[0]);
	}
	
	public MessageLocalized(String messageCode, Object... messageParams) {
		this.messageCode = messageCode;
		this.messageParams = messageParams;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public Object[] getMessageParams() {
		return messageParams;
	}

	@Override
	public String toString() {
		return "MessageLocalized [messageCode=" + messageCode + ", messageParams=" + Arrays.toString(messageParams) + "]";
	}
		
}
