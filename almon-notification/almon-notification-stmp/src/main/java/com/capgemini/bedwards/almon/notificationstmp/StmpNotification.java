package com.capgemini.bedwards.almon.notificationstmp;

import com.capgemini.bedwards.almon.almondatastore.models.alert.Alert;
import com.capgemini.bedwards.almon.notificationcore.Notification;
import com.capgemini.bedwards.almon.notificationcore.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Slf4j
@Component
public class StmpNotification implements Notification {

    private final NotificationService NOTIFICATION_SERVICE;
    private final Session SESSION;

    private final NotificationSTMPConfig CONFIG;


    @Autowired
    public StmpNotification(NotificationService notificationService, NotificationSTMPConfig notificationSTMPConfig) {
        this.NOTIFICATION_SERVICE = notificationService;
        this.CONFIG = notificationSTMPConfig;

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.host", notificationSTMPConfig.getHost());
        prop.put("mail.smtp.port", notificationSTMPConfig.getPort());

        SESSION = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(notificationSTMPConfig.getUsername(), notificationSTMPConfig.getPassword());
            }
        });
    }

    @Override
    public void sendNotification(Alert alert) {
        sendEmail("ben.edwards2000@live.co.uk", alert.getStatus() + " - " + alert.getMonitor().getId() + " - " + alert.getMonitor().getName(), alert.getHTMLMessage());
    }


    public void sendEmail(String toEmail, String subject, String body) {
        if (log.isDebugEnabled())
            log.debug("Sending email with subject " + subject + " to : " + toEmail);
        try {
            Message message = new MimeMessage(this.SESSION);
            message.setFrom(new InternetAddress(this.CONFIG.getUsername()));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(CONFIG.getSubjectPrefix() + subject);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
