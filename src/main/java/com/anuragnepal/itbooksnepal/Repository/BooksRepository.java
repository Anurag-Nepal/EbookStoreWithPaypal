package com.anuragnepal.itbooksnepal.Repository;

import com.anuragnepal.itbooksnepal.Entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Books,Integer> {

    List<Books> findByCategory(String categoryName);

}
