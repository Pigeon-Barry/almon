package com.capgemini.bedwards.almon.almonnotificationsms;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.almondatastore.models.auth.User;
import com.capgemini.bedwards.almon.almondatastore.models.contract.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
public class SmsNotification implements Notification {

    private static final String URL_TEMPLATE = "https://graph.facebook.com/v13.0/{phoneNumber}/messages";
    private static final String REQUEST_BODY_TEMPLATE = "{\"messaging_product\":\"whatsapp\",\"to\":\"%PHONE_NUMBER%\",\"type\":\"template\",\"template\":{\"name\":\"almon_alert\",\"language\":{\"code\":\"en_GB\"},\"components\":[{\"type\":\"header\",\"parameters\":[{\"type\":\"text\",\"text\":\"%TITLE%\"}]},{\"type\":\"body\",\"parameters\":[{\"type\":\"text\",\"text\":\"%BODY%\"}]}]}}";

    private final NotificationSMSConfig CONFIG;
    private final String URL;

    @Autowired
    public SmsNotification(NotificationSMSConfig notificationSMSConfig) {
        this.CONFIG = notificationSMSConfig;
        this.URL = URL_TEMPLATE.replace("{phoneNumber}", this.CONFIG.getPhoneId());
    }

    @Override
    public void sendNotification(Set<User> subscribedUsers, Alert<?> alert) {
        for (User user : subscribedUsers) {
            sendText(user.getPhoneNumber(),
                    alert.getStatus() + " - " + alert.getMonitor().getId() + " - " + alert.getMonitor()
                            .getName(), alert.getShortMessage());
        }
    }


    private void sendText(String phoneNumber, String title, String content) {
        if (phoneNumber == null)
            return;
        phoneNumber = phoneNumber.replaceAll("\\+", "");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(this.CONFIG.getAccessToken());


        HttpEntity<String> entity = new HttpEntity<>(REQUEST_BODY_TEMPLATE.replace("%PHONE_NUMBER%", phoneNumber).replace("%TITLE%", title).replace("%BODY%", content), headers);
        ResponseEntity<?> result = restTemplate.exchange(this.URL, HttpMethod.POST, entity, String.class);
        assert result.getStatusCode().is2xxSuccessful();
    }

    @Override
    public String getDisplayName() {
        return "SMS";
    }

    @Override
    public String getHelpText() {
        return "Upon a failed check. This will send an text message with the failure details to the registered phone number on this account";
    }

    @Override
    public String getId() {
        return "SMS";
    }

}
