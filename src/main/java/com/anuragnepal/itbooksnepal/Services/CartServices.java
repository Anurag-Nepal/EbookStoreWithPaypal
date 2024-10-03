package com.anuragnepal.itbooksnepal.Services;
import com.anuragnepal.itbooksnepal.Entity.Books;
import com.anuragnepal.itbooksnepal.Entity.Cart;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartDto;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartHelper;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartItemDto;
import com.anuragnepal.itbooksnepal.Entity.Users;
import com.anuragnepal.itbooksnepal.Repository.BooksRepository;
import com.anuragnepal.itbooksnepal.Repository.CartRepository;
import com.anuragnepal.itbooksnepal.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServices {
    @Autowired
    EmailService emailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookService bookService;
    @Autowired
    BooksRepository booksRepository;
    @Autowired
    CartRepository cartRepository;

    public String getcurrentusername()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }

public Cart addToCart( CartHelper cartHelper)
{
    Users user = userRepository.findByUsername(getcurrentusername());
    Books books = booksRepository.findById(cartHelper.getBid()).orElse(null);

    Cart cart = new Cart();
    cart.setBooks(books);
    cart.setUsers(user);
    cart.setCreateDate(LocalDate.now());
    cartRepository.save(cart);
    return cart;
    }


    public CartDto showCartOfUser()
    {
        Users user= userRepository.findByUsername(getcurrentusername());

        List<Cart> cartList = cartRepository.findByUsers_IdOrderByCreateDateDesc(user.getId());
        List<CartItemDto> cartItems = new ArrayList<>();
        Integer totalPrice = 0;
        for(Cart cart:cartList)
        {
CartItemDto cartItemDto = new CartItemDto(cart);
cartItems.add(cartItemDto);
totalPrice+=cart.getBooks().getPrice();

        }
        CartDto cartDto=new CartDto();
        cartDto.setCartList(cartItems);
        cartDto.setPrice(totalPrice);
        return cartDto;
    }



    public void deleteItem(Integer cartid) throws Exception {
        Users loggedUser =userRepository.findByUsername(getcurrentusername());
       Optional<Cart> optcart = cartRepository.findById(cartid);
        if(optcart.isEmpty())
        {
            throw new Exception("Invalid Product Id Provided");
        }

        Cart cart = optcart.get();
        if(!cart.getUsers().equals(loggedUser))
        {
            throw new IllegalArgumentException("Sorry This Product Is not Associated to this user");
        }

        cartRepository.delete(cart);

    }
}
