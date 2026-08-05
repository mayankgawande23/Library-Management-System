package com.library.dao;

import com.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    void addBook(Book book);
    void updateBook(Book book);
    void deleteBook(int bookId);
    Optional<Book> findById(int bookId);
    List<Book> searchBooks(String keyword);
    List<Book> listAllBooks();
}
