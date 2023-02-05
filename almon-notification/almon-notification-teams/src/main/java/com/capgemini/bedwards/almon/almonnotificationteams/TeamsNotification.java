package com.capgemini.bedwards.almon.almonnotificationteams;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import com.google.gson.JsonPrimitive;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.ConversationMemberCollectionPage;
import com.microsoft.graph.requests.ConversationMemberCollectionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Set;

@Slf4j
@Component
public class TeamsNotification implements Notification {

    private final Graph GRAPH;

    @Autowired
    public TeamsNotification(NotificationTeamsConfig notificationSTMPConfig) {
        this.GRAPH = new Graph(notificationSTMPConfig);
    }

    @Override
    public void sendNotification(Set<User> subscribedUsers, Alert<?> alert) {
        ChatMessage chatMessage = new ChatMessage();
        ItemBody body = new ItemBody();
        body.content = alert.getMessage();
        chatMessage.body = body;

        for (User user : subscribedUsers) {
            Chat chat = new Chat();
            chat.chatType = ChatType.ONE_ON_ONE;
            LinkedList<ConversationMember> membersList = new LinkedList<>();
            AadUserConversationMember members1 = new AadUserConversationMember();
            LinkedList<String> rolesList1 = new LinkedList<>();
            rolesList1.add("owner");
            members1.roles = rolesList1;
            members1.additionalDataManager().put("user@odata.bind", new JsonPrimitive("https://graph.microsoft.com/v1.0/users('" + user.getEmail() + "')"));
            membersList.add(members1);
            ConversationMemberCollectionResponse conversationMemberCollectionResponse = new ConversationMemberCollectionResponse();
            conversationMemberCollectionResponse.value = membersList;
            chat.members = new ConversationMemberCollectionPage(conversationMemberCollectionResponse, null);

            chat = this.GRAPH.getGraphClient().chats()
                    .buildRequest()
                    .post(chat);
            assert chat.id != null;
            this.GRAPH.getGraphClient().chats(chat.id).messages()
                    .buildRequest()
                    .post(chatMessage);
        }
    }

    @Override
    public String getDisplayName() {
        return "Teams";
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Upon a failed check. This will send an teams message with the failure details to the configured teams chat";
    }

    @Override
    public String getId() {
        return "TEAMS";
    }


}
