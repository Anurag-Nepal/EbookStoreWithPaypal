package com.anuragnepal.itbooksnepal.Extras;

import com.anuragnepal.itbooksnepal.Entity.Users;
import com.anuragnepal.itbooksnepal.Services.EmailService;
import com.anuragnepal.itbooksnepal.Services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BulkMessage {

    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;

    public void bulkClients(BulkMailExtras a) throws MessagingException {
        for (Users user : userService.getAllUsers()) {
try{
    String to = user.getEmail();
    String title = a.getTitle();
    String message = a.getMessage();
    emailService.SendEmail(to, title, message);

} catch (MessagingException e) {
    throw new RuntimeException("Sorry Mate U Messed Up Somewhere");
}
        }

    }
}
