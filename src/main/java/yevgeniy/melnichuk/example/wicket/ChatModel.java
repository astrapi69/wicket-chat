package yevgeniy.melnichuk.example.wicket;

import java.io.Serializable;
import java.util.LinkedList;

public class ChatModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private final LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>();
	private int visibleMessages = 50;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<ChatMessage> getMessages() {
		return messages;
	}

	public int getVisibleMessages() {
		return visibleMessages;
	}

	public void setVisibleMessages(int visibleMessages) {
		this.visibleMessages = visibleMessages;
	}

}
