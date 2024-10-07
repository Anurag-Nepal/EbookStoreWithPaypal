package com.anuragnepal.itbooksnepal.Paypal;

import com.anuragnepal.itbooksnepal.Entity.Helpers.CartDto;
import com.anuragnepal.itbooksnepal.Services.CartServices;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class PaypalController {
    @Autowired
    CheckOut checkOut;


    @PostMapping(value = "/make/payment")
    public Map<String, Object> makePayment(){
        return checkOut.createPayment();
    }


    @GetMapping("/complete/payment")
    public Map<String, Object> completePayment(HttpServletRequest request, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        return checkOut.completePayment(request);
    }

    @GetMapping("/cancel")
    public String cancel()
    {
        return "Cancelled  the Payment";
    }
    @GetMapping("/hey")
    public String hey()
    {

        return "Hey Welcome to the Site";
    }
}
