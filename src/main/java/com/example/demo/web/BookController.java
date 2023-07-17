package com.example.demo.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;

import com.example.demo.persistence.model.Book;
import com.example.demo.persistence.repo.BookRepository;
import com.example.demo.service.BookService;
import com.example.demo.web.exception.BookIdMismatchException;
import com.example.demo.web.exception.BookNotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @GetMapping
    public CompletableFuture<List<Book>> findAll() {
      return bookService.findAll();
    }

    @GetMapping("/title/{bookTitle}")
    @Async
    public CompletableFuture<List<Book>> findByTitle(@PathVariable String bookTitle) {
      return bookService.findByTitle(bookTitle);
    }

    @GetMapping("/{id}")
    @Async
    public CompletableFuture<Book> findOne(@PathVariable Long id) {
      return bookService.findOne(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Async
    public CompletableFuture<Book> create(@Valid @RequestBody Book book) {
      return bookService.create(book); 
    }

    @DeleteMapping("/{id}")
    @Async
    public CompletableFuture<Void> delete(@PathVariable Long id) {
      return bookService.delete(id);
    }

    @PutMapping("/{id}")
    @Async
    public CompletableFuture<Book> updateBook(@RequestBody Book book, @PathVariable Long id) {
      return bookService.updateBook(book, id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}