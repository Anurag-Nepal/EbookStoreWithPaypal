package com.anuragnepal.itbooksnepal.Services;
import com.anuragnepal.itbooksnepal.Entity.UserPrincipal;
import com.anuragnepal.itbooksnepal.Entity.Users;
import com.anuragnepal.itbooksnepal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user != null) {
            return new UserPrincipal(user.getUsername(), user.getPassword(), user.getRole().name());

        }
        throw new UsernameNotFoundException("Username Cannot Be Found");

    }
}
