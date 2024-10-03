package com.anuragnepal.itbooksnepal.Repository;

import com.anuragnepal.itbooksnepal.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);
}
