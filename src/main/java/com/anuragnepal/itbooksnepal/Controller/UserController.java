package com.anuragnepal.itbooksnepal.Controller;

import com.anuragnepal.itbooksnepal.Entity.Helpers.OtpService;
import com.anuragnepal.itbooksnepal.Entity.Users;
import com.anuragnepal.itbooksnepal.Services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

@PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users users) throws MessagingException {
        String token = userService.registerUser(users);
        return new ResponseEntity<>(token,HttpStatus.CREATED);
    }

    @PostMapping("/login")

    public  ResponseEntity<String>login(@RequestBody Users user)
    {
        String login = userService.login(user);
        return new ResponseEntity<>(login,HttpStatus.ACCEPTED);

    }
@PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody OtpService otpService)
{
    userService.otpVerify(otpService);
    return new ResponseEntity<>("Otp Successfully Verified",HttpStatus.ACCEPTED);
}
}
