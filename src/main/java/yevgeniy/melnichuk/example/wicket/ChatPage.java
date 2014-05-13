package yevgeniy.melnichuk.example.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import wicket.chat.service.ServiceLocator;

public class ChatPage extends WebPage {
    private static final long serialVersionUID = 1L;

    public ChatPage() {
    	IModel<ChatModel> model = ServiceLocator.getChatService().getChatRoom("public");
    	ChatPanel chatPanel = new ChatPanel("chatPanel", model);
        add(buildUsernameLabel(), chatPanel);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        String username = ChatSession.get().getUsername();
        if (username == null) {
            throw new RestartResponseAtInterceptPageException(UsernamePage.class);
        }
    }

    private Component buildUsernameLabel() {
        String username = ChatSession.get().getUsername();
        Model<String> model = new Model<String>(username);
        Label usernameLabel = new Label("username", model);
        return usernameLabel;
    }

}
