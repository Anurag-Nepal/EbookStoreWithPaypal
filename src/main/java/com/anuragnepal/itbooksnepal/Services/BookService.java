package com.anuragnepal.itbooksnepal.Services;

import com.anuragnepal.itbooksnepal.Entity.Books;
import com.anuragnepal.itbooksnepal.Repository.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    BooksRepository booksRepository;
    // Adding New Book
    public void addNewBook(Books books, MultipartFile pdfFile, MultipartFile imgFile) throws IOException {
        Books booksToSave = new Books();
        if (books.getName() != null) {
            booksToSave.setName(books.getName());
        } else {
            throw new IllegalArgumentException("Book name cannot be null");
        }
        booksToSave.setDescription(books.getDescription());
        booksToSave.setPrice(books.getPrice());
        booksToSave.setRating(0.00);
        booksToSave.setCategory(books.getCategory());
        if (pdfFile != null && !pdfFile.isEmpty()) {
            booksToSave.setPdfData(pdfFile.getBytes());
        } else {
            throw new IllegalArgumentException("PDF file cannot be empty");
        }

        if (imgFile != null && !imgFile.isEmpty()) {
            booksToSave.setImgData(imgFile.getBytes());
        } else {
            throw new IllegalArgumentException("Image file cannot be empty");
        }

        booksRepository.save(booksToSave);
    }
//Deleting a Book
    public void deleteBook( Integer id)
    {
 booksRepository.deleteById(id);

    }


    //Edit Book
    public void editDetails(Integer id,Books books)
    {
        Books books1 = booksRepository.findById(id).get();
        books1.setName(books.getName());
        books1.setPrice(books.getPrice());
        books1.setDescription(books.getDescription());
        booksRepository.save(books1);
    }

    //Load the Books List acording to Category
    public List<Books>getList(String cname)
    {
       return booksRepository.findByCategory(cname);
    }


    //Retreiving a book from database

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }


    public Books getById(Integer id)
    {
        Books books=booksRepository.findById(id).orElse(null);
        return books;

    }




}
