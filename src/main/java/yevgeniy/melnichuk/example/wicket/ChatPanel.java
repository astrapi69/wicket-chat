package yevgeniy.melnichuk.example.wicket;

import java.util.LinkedList;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import wicket.chat.service.FocusRequestBehavior;

public class ChatPanel extends Panel {
    private static final long serialVersionUID = 1L;

    private final int visibleMessages;
    
    private final LinkedList<ChatMessage> messages;

    private final Component messagesContainer;
    	/** The form. */
	private final Form<?> form;
	
	private final TextField<String> textField;
	
	private String currentMessage;
	
	public String getCurrentMessage() {
		return currentMessage;
	}

	public TextField<String> getTextField() {
		return textField;
	}


	private final Component submitButton;
	
	public ChatPanel(String id, IModel<ChatModel> model) {
		super(id, model);
		messages = model.getObject().getMessages();
		this.visibleMessages = model.getObject().getVisibleMessages();
		add(messagesContainer = newMessagesContainer("messages"));
        
        add(form = newChatForm("form", model));
        currentMessage = "";
		textField = newTextField("message", new PropertyModel<String>(this, "currentMessage"));
		
        submitButton = newSubmitButton("submit", model);        		
        
        form.add(textField, submitButton);
        
	}
	
	protected Component newSubmitButton(String id, IModel<?> model) {
		AjaxSubmitLink submitButton = new AjaxSubmitLink("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                String username = ChatSession.get().getUsername();
                String text = textField.getModelObject();

                ChatMessage chatMessage = new ChatMessage(username, text);

                synchronized (messages) {
                    if (messages.size() >= visibleMessages) {
                        messages.removeLast();
                    }
                    messages.addFirst(chatMessage);
                }
                textField.setDefaultModelObject("");
                textField.setModelObject("");
                target.add(messagesContainer, getTextField(), form);

        		textField.add(new FocusRequestBehavior(true));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                throw new UnsupportedOperationException("nor errors allowed :)");
            }
        };
        return submitButton;
	}
	
	protected TextField<String> newTextField(String id, IModel<String> model) {
		TextField<String> textField = new TextField<String>(id, model);
        textField.setOutputMarkupId(true);
        textField.setOutputMarkupPlaceholderTag(true);
		return textField;
	}
	
	protected Form<?> newChatForm(String id, IModel<?> model){
		Form<String> f = new Form<String>(id);
		f.setOutputMarkupId(true);
		return f;		
	}
	

    protected Component newMessagesContainer(String id) {
    	MarkupContainer messagesContainer = new WebMarkupContainer(id);

        final ListView<ChatMessage> listView = new ListView<ChatMessage>("messageListView", messages) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ChatMessage> item) {
                this.modelChanging();

                ChatMessage message = item.getModelObject();

                Label from = new Label("from", new PropertyModel<String>(message, "from"));
                item.add(from);

                Label text = new Label("text", new PropertyModel<String>(message, "text"));
                item.add(text);
            }
        };

        messagesContainer.setOutputMarkupId(true);
        messagesContainer.add(listView);

        AjaxSelfUpdatingTimerBehavior ajaxBehavior = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1));
        messagesContainer.add(ajaxBehavior);

        return messagesContainer;
    }

}
