package com.anuragnepal.itbooksnepal.Entity.Helpers;

import com.anuragnepal.itbooksnepal.Entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Data

@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
  private Double price;

    private List<CartItemDto> cartList;
}
