package com.anuragnepal.itbooksnepal.Services;

import com.anuragnepal.itbooksnepal.Entity.Helpers.OtpService;
import com.anuragnepal.itbooksnepal.Entity.UserRole;
import com.anuragnepal.itbooksnepal.Entity.Users;
import com.anuragnepal.itbooksnepal.Repository.OtpRepository;
import com.anuragnepal.itbooksnepal.Repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    OtpRepository otpRepository;
    @Autowired
    EmailService emailService;

    public String registerUser(Users user) throws MessagingException {
        Users users = new Users();
        users.setEmail(user.getEmail());
        users.setUsername(user.getUsername());
        users.setPassword(passwordEncoder.encode(user.getPassword()));
        users.setFullName(user.getFullName());
        users.setRole(UserRole.USER);
        userRepository.save(users);
        OtpService otp = new OtpService();
        otp.setOtp(generateOtp());
        otp.setUsers(users);
        otpRepository.save(otp);
        String to = user.getEmail();
        String sub = "Otp Verification Mail For Ebook Store \n";
        String body = "Hey Welcome to Ebook Nepal \n " +
                "You are Receiving this Mail For Verifiying Your Request To Register in Our Platform. \n" +
                "Your Otp is " + otp +
                "Thank You For Choosing Us \n" +
                "Warm Regards" +
                "Anurag Nepal\n" +
                "Ebook Store Nepal" +
                "anuragnepal2062@gmail.com";
        try {
            emailService.SendEmail(to, sub, body);
        } catch (MessagingException e) {
            throw new MessagingException("Sorry The Verification Could not Complete");

        }

        return jwtService.generateToken(user.getUsername());
    }


    public String otpVerify(OtpService otpService)
    {
        Users user = otpService.getUsers();
       Integer dbOtp =otpRepository.findOtpByUsername(user.getUsername());
        Integer userOtp = otpService.getOtp();
        if(dbOtp.equals(userOtp))
        {
            otpRepository.deleteById(dbOtp);
            return "Otp Verified Successfully";
        }
        else
            throw new IllegalArgumentException("Otp Incorrect");
    }


    public String login(Users users)
    {
        Users existing =userRepository.findByUsername(users.getUsername());
        if(existing==null)
        {
            return  "User Not Found";
        }

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        if (authentication.isAuthenticated())
            return jwtService.generateToken(users.getUsername());
        else
            return "Authentication Failed";

    }

    public Integer generateOtp()
    {
        Random random = new Random();
        Integer otp = random.nextInt(999999);
        return otp;
    }


    public List<Users> getAllUsers()
    {
        return userRepository.findByRole(UserRole.USER);

    }






}
