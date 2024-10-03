package com.anuragnepal.itbooksnepal.Repository;

import com.anuragnepal.itbooksnepal.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    List<Cart> findByUsers_IdOrderByCreateDateDesc(Integer userId);

}
