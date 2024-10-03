package com.anuragnepal.itbooksnepal.Entity.Helpers;

import com.anuragnepal.itbooksnepal.Entity.Books;
import com.anuragnepal.itbooksnepal.Entity.Cart;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
@Data
@NoArgsConstructor
public class CartItemDto {
    private Integer id;

    private Books books;

    public CartItemDto(Cart cart) {
        this.id = cart.getCartId();
        this.setBooks(cart.getBooks());
    }
}
