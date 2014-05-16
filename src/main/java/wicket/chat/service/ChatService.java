package wicket.chat.service;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import yevgeniy.melnichuk.example.wicket.ChatModel;

/**
 * The Class ChatService is holding all chat rooms in a map.
 */
public class ChatService
{
	
	/** The chatrooms by name. */
	private final Map<String, IModel<ChatModel>> chatroomsByName = new HashMap<String, IModel<ChatModel>>();

	/**
	 * Gets the chat room by the name of it. 
	 *
	 * @param name the name from the chatroom.
	 * @return the ChatRoom object.
	 */
	public IModel<ChatModel> getChatRoom(final String name)
	{
		IModel<ChatModel> room = getRoom(name);
		if (room == null)
		{
			ChatModel chatModel = new ChatModel();
			chatModel.setName(name);
			room = Model.of(chatModel);
			chatroomsByName.put(name, room);
		}
		return room;
	}

	private IModel<ChatModel> getRoom(final String name) {
		IModel<ChatModel> room = chatroomsByName.get(name);
		return room;
	}
	
	public boolean exists(final String name) {
		return chatroomsByName.containsKey(name);
	}
}