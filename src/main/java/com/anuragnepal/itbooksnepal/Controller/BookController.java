package com.anuragnepal.itbooksnepal.Controller;

import com.anuragnepal.itbooksnepal.Entity.Books;
import com.anuragnepal.itbooksnepal.Entity.Cart;
import com.anuragnepal.itbooksnepal.Services.BookService;
import com.anuragnepal.itbooksnepal.Services.CartServices;
import com.anuragnepal.itbooksnepal.Services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/books")
@RestController
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    EmailService emailService;
    @Autowired
    CartServices cart;

    @PostMapping("/addBook")
    public ResponseEntity<?> addNewBook(@ModelAttribute Books books,
                                        @RequestPart MultipartFile pdfFile,
                                        @RequestPart MultipartFile imgFile) throws IOException
    {

        bookService.addNewBook(books,pdfFile,imgFile);
        return  new ResponseEntity<>("Book Added SuccessFully", HttpStatus.OK);


    }

//All books with data,image and pdf
    @GetMapping("/listall")
    public ResponseEntity<List<Books>> getAlll()
    {
        List<Books> books = bookService.getAllBooks(); // Fetch all books
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

//Getting books by id
    @GetMapping("/book/{id}")
    public ResponseEntity<Books> getById(@PathVariable Integer id)
    {
        Books books=bookService.getById(id);
        if(books.equals(null))
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(books,HttpStatus.OK);


    }
    //Retrieving the Image of Specific Book By id
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getBookImage(@PathVariable Integer id) {
        Books book = bookService.getById(id);
        if (book == null || book.getImgData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/jpeg"); // Adjust based on your image type
        return new ResponseEntity<>(book.getImgData(), headers, HttpStatus.OK);
    }

    // Endpoint to retrieve the PDF of a specific book by ID
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getBookPdf(@PathVariable Integer id) {
        Books book = bookService.getById(id);
        if (book == null || book.getPdfData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/pdf");
        return new ResponseEntity<>(book.getPdfData(), headers, HttpStatus.OK);
    }

    //Sending the book into Email
    @GetMapping("/send/{id}")
    public String send(@PathVariable Integer id) throws MessagingException {
        Books book = bookService.getById(id);

        if (book == null || book.getPdfData() == null) {
            return "Book not found or PDF data is missing.";
        }

        String pdfFileName = book.getName() + ".pdf";
        byte[] pdfData = book.getPdfData();
        String to = "anuragnepal99@gmail.com";
        String subject = "Hey";
        String body = "Hello Email Sent";
        emailService.sendEmailAttachment(to, subject, body, pdfData, pdfFileName);

        return "Success";
    }
//    @GetMapping("/send")
//    public String send() throws MessagingException {
//        cart.Checkout();
//        return "Success";
//    }




}