package com.example.demo.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.persistence.model.Book;
import com.example.demo.persistence.repo.BookRepository;
import com.example.demo.web.exception.BookIdMismatchException;
import com.example.demo.web.exception.BookNotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Async("serviceExecutor")
    public CompletableFuture<List<Book>> findAll() {
      return CompletableFuture.supplyAsync(() -> bookRepository.findAll());
    }

    @Async("serviceExecutor")
    public CompletableFuture<List<Book>> findByTitle(String bookTitle) {
      return CompletableFuture.supplyAsync(() -> bookRepository.findByTitle(bookTitle));
    }

    @Async("serviceExecutor")
    public CompletableFuture<Book> findOne(Long id) {
      return CompletableFuture.supplyAsync(() -> {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
      });
    }

    @Async("serviceExecutor")
    public CompletableFuture<Book> create(@Valid Book book) {
      return CompletableFuture.supplyAsync(() -> {
        return bookRepository.save(book);
      });   
    }

    @Async("serviceExecutor")
    @Transactional
    public CompletableFuture<Void> delete(Long id) {
        //return CompletableFuture.supplyAsync(() -> {
            //will it be executed inside a transaction? NOPE
            bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
            bookRepository.deleteById(id);
            //return null;
        //});

        return CompletableFuture.completedFuture(null);
    }

    @Async("serviceExecutor")
    @Transactional
    public CompletableFuture<Book> updateBook(@Valid Book book, Long id) {
       //return CompletableFuture.supplyAsync(() -> {
            if (book.getId() != id) {
                throw new BookIdMismatchException("Failed update, book id mismatch");
            }
            Book existingBook = bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);

            existingBook.setAuthor(book.getAuthor());
            existingBook.setTitle(book.getTitle());
            
            Book updatedBook = bookRepository.save(existingBook);

            if (book.getAuthor().equals("boom")) {
                throw new BookIdMismatchException("Boom");
            }

            //return updatedBook;
        //});

        return CompletableFuture.completedFuture(updatedBook);
    }
}
