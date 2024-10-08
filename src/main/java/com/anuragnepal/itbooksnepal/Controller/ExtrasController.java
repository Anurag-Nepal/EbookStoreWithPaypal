package com.anuragnepal.itbooksnepal.Controller;

import com.anuragnepal.itbooksnepal.Extras.BulkMailExtras;
import com.anuragnepal.itbooksnepal.Extras.BulkMessage;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExtrasController {
    @Autowired
    BulkMessage bulkMessage;

    @PostMapping("/sendbulk")
    public String send(@RequestBody BulkMailExtras bm) throws MessagingException {
      bulkMessage.bulkClients(bm);
        return "Great Success";
    }
}
