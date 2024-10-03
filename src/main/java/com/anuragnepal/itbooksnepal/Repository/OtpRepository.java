package com.anuragnepal.itbooksnepal.Repository;

import com.anuragnepal.itbooksnepal.Entity.Helpers.OtpService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OtpRepository extends JpaRepository<OtpService,Integer> {

    @Query("SELECT o.otp FROM OtpService o JOIN o.users u WHERE u.username = :username")
    Integer findOtpByUsername(@Param("username") String username);
}
