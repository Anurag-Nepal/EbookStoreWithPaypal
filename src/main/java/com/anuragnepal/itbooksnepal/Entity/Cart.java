package com.anuragnepal.itbooksnepal.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;
    @ManyToOne
    @JoinColumn(name = "bid")
    private Books books;
    @ManyToOne
    @JoinColumn(name = "uid")
    private Users users;
    @Column
    private LocalDate createDate ;
    public Cart(Books books, Users users) {
        this.books = books;
        this.users = users;
    }
}
