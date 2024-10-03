package com.anuragnepal.itbooksnepal.Paypal;

import com.anuragnepal.itbooksnepal.Entity.Helpers.CartDto;
import com.anuragnepal.itbooksnepal.Services.CartServices;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PaypalController {
    @Autowired
    CheckOut checkOut;
    @Autowired
    CartServices cartServices;
@GetMapping("/token")
    private ResponseEntity<?> generateToken() throws IOException {
        String token = checkOut.generateToken();
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @PostMapping("/api/orders")
public ResponseEntity<JsonNode> createOrder()
{
    JsonNode order = checkOut.createOrder(cartServices.showCartOfUser()).getBody();
    return new ResponseEntity<>(order,HttpStatus.OK);
}


    @PostMapping("v2/checkout/orders/{orderID}/capture")
    public ResponseEntity < JsonNode > captureOrder( ) throws IOException {
        String orderID = checkOut.createPaypalOrder(cartServices.showCartOfUser()).get("id").toString();
     JsonNode node = checkOut.captureOrder(orderID).getBody();
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @GetMapping("/success")
    public String success()
    {
        return "Success";
    }

    @GetMapping("/cancel")
    public String cancel()
    {
        return "cancel";
    }

}
