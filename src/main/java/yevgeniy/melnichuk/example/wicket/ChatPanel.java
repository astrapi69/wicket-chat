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
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

public class ChatPanel extends GenericPanel<ChatModel> {
    private static final long serialVersionUID = 1L;

//    private static final int MAX_MESSAGES = 50;
//    static private final LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>();

    private final Component messagesContainer;
    	/** The form. */
	private final Form<?> form;
	
	private final TextField<String> textField;
	
	private final Component submitButton;
    
	public ChatPanel(String id, IModel<ChatModel> model) {
		super(id, model);
		
		add(messagesContainer = newMessagesContainer("messages", model));
        
        add(form = newChatForm("form", model));
        
		textField = newTextField("message", new Model<String>());
		
        submitButton = newSubmitButton("submit", model);        		
        
        form.add(textField, submitButton);
        
	}
	
	protected Component newSubmitButton(final String id, final IModel<ChatModel> model) {
		AjaxSubmitLink submitButton = new AjaxSubmitLink("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                String username = getUsername();
                String text = textField.getModelObject();

                ChatMessage chatMessage = new ChatMessage(username, text);

                synchronized(model.getObject().getMessages()) {
                    if (model.getObject().getMessages().size() >= model.getObject().getVisibleMessages()) {
                    	model.getObject().getMessages().removeLast();
                    }
                    model.getObject().getMessages().addFirst(chatMessage);
                }

                textField.setModelObject("");
                target.add(messagesContainer, textField);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                throw new UnsupportedOperationException("nor errors allowed :)");
            }
        };
        return submitButton;
	}

	protected String getUsername() {
		return ChatSession.get().getUsername();
	}
	
	protected TextField<String> newTextField(String id, final IModel<String> model) {
		TextField<String> textField = new TextField<String>(id, model);
        textField.setOutputMarkupId(true);
		return textField;
	}
	
	protected Form<?> newChatForm(String id, final IModel<?> model){
		return new Form<String>(id);		
	}
	

    protected Component newMessagesContainer(String id, final IModel<ChatModel> model) {
    	MarkupContainer messagesContainer = new WebMarkupContainer(id);

        final ListView<ChatMessage> listView = new ListView<ChatMessage>("messageListView", model.getObject().getMessages()) {
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

        AjaxSelfUpdatingTimerBehavior ajaxBehavior = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5));
        messagesContainer.add(ajaxBehavior);

        return messagesContainer;
    }

}