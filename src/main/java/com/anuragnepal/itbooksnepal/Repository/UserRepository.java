package com.anuragnepal.itbooksnepal.Repository;

import com.anuragnepal.itbooksnepal.Entity.UserRole;
import com.anuragnepal.itbooksnepal.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);

    List<Users> findByRole(UserRole roleName);
}
