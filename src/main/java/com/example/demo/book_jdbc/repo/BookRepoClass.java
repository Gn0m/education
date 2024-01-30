package com.example.demo.book_jdbc.repo;

import com.example.demo.book_jdbc.model.Book;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepoClass implements BookRepo {

    private final JdbcTemplate jdbcTemplate;

    public BookRepoClass(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int save(Book book) {
        return jdbcTemplate.update("INSERT INTO book (title,author,publicationYear) VALUES(?,?,?)",
                book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    @Override
    public int update(Book book) {
        return jdbcTemplate.update("UPDATE book SET title=?, author=?, publicationYear=? WHERE id=?",
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());
    }

    @Override
    public Book findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM book WHERE id=?",
                BeanPropertyRowMapper.newInstance(Book.class), id);
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * from book", BeanPropertyRowMapper.newInstance(Book.class));
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return jdbcTemplate.query("SELECT * from book WHERE author=?",
                BeanPropertyRowMapper.newInstance(Book.class), author);
    }

    @Override
    public List<Book> findByTitleContaining(String title) {
        String q = "SELECT * from book WHERE title ILIKE '%" + title + "%'";
        return jdbcTemplate.query(q, BeanPropertyRowMapper.newInstance(Book.class));
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update("DELETE from book");
    }
}
