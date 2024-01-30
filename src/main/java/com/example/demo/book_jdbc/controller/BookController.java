package com.example.demo.book_jdbc.controller;

import com.example.demo.book_jdbc.model.Book;
import com.example.demo.book_jdbc.repo.BookRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepo repo;

    public BookController(BookRepo repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Book> findAll() {
        return repo.findAll();
    }

    @GetMapping("/author")
    public ResponseEntity<List<Book>> findByAuthor(@RequestParam String author) {
        List<Book> byAuthor = repo.findByAuthor(author);
        return new ResponseEntity<>(byAuthor, HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<List<Book>> findByTitle(@RequestParam String title) {
        List<Book> listByTitle = repo.findByTitleContaining(title);
        return new ResponseEntity<>(listByTitle, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable(value = "id") long id) {
        return repo.findById(id);
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody Book book) {
        int save = repo.save(book);
        return new ResponseEntity<>("Created: " + save, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Book book) {
        int update = repo.update(book);
        return new ResponseEntity<>("Updated: " + update, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
        repo.deleteById(id);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteAll() {
        repo.deleteAll();
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

}
