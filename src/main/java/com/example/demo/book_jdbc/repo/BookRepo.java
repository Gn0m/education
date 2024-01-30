package com.example.demo.book_jdbc.repo;

import com.example.demo.book_jdbc.model.Book;

import java.util.List;

public interface BookRepo {

    int save(Book book);

    int update(Book book);

    Book findById(Long id);

    int deleteById(Long id);

    List<Book> findAll();

    List<Book> findByAuthor(String author);

    List<Book> findByTitleContaining(String title);

    int deleteAll();
}
