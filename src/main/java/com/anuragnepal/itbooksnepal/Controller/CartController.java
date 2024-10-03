package com.anuragnepal.itbooksnepal.Controller;

import com.anuragnepal.itbooksnepal.Entity.Cart;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartDto;
import com.anuragnepal.itbooksnepal.Entity.Helpers.CartHelper;
import com.anuragnepal.itbooksnepal.Services.CartServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
    @Autowired
    CartServices cartServices;

    @PostMapping("/hello")
  public ResponseEntity<Cart> addToCart(@RequestBody CartHelper cartHelper)
  {
      Cart cart = cartServices.addToCart(cartHelper);
      return new ResponseEntity<>(cart,HttpStatus.OK);
  }

@GetMapping("/hi")
  public ResponseEntity<CartDto> getAllItems()
  {
      CartDto cartDto = cartServices.showCartOfUser();
      return new ResponseEntity<>(cartDto,HttpStatus.OK);
  }

  @DeleteMapping("/delete/{cid}")
    public ResponseEntity<String> deleteCartItems(@PathVariable Integer cid) throws Exception {

      cartServices.deleteItem(cid);
      return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
    }



}


