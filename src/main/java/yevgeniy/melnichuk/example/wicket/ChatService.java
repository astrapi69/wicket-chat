package yevgeniy.melnichuk.example.wicket;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

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
		IModel<ChatModel> room = chatroomsByName.get(name);
		if (room == null)
		{
			ChatModel chatModel = new ChatModel();
			chatModel.setName(name);
			room = Model.of(chatModel);
			chatroomsByName.put(name, room);
		}
		return room;
	}
}