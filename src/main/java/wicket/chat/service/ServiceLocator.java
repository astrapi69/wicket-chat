package wicket.chat.service;

import yevgeniy.melnichuk.example.wicket.ChatService;


public class ServiceLocator
{

	private static final ChatService chatService = new ChatService();

	public static ChatService getChatService()
	{
		return chatService;
	}

	private ServiceLocator()
	{
		super();
	}
}