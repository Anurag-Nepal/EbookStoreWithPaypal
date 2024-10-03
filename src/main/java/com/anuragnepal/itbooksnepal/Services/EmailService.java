package com.anuragnepal.itbooksnepal.Services;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendEmailAttachment(String to, String subject, String body, byte[] pdfData, String pdfFileName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("Pennywisenepal@gmail.com");
        helper.setText(body, true); // Set to true if the body is HTML

        // Add PDF attachment
        if (pdfData != null && pdfData.length > 0) {
            helper.addAttachment(pdfFileName, new ByteArrayDataSource(pdfData, "application/pdf"));
        }

        // Send the email
        javaMailSender.send(message);
    }

    public void SendEmail(String to,String sub,String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(sub);
        message.setFrom(new InternetAddress("Pennywisenepal@gmail.com"));
        message.setText(body,"UTF-8");
        javaMailSender.send(message);


    }
}
