package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.impl.BookDAOImpl;
import com.library.exception.BookNotAvailableException;
import com.library.model.Book;

import java.util.List;
import java.util.Optional;

public class BookService {
    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAOImpl();
    }

    public void addBook(Book book) {
        bookDAO.addBook(book);
    }

    public void updateBook(Book book) {
        bookDAO.updateBook(book);
    }

    public void deleteBook(int bookId) {
        bookDAO.deleteBook(bookId);
    }

    public Optional<Book> findById(int bookId) {
        return bookDAO.findById(bookId);
    }

    public List<Book> searchBooks(String keyword) {
        return bookDAO.searchBooks(keyword);
    }

    public List<Book> listAllBooks() {
        return bookDAO.listAllBooks();
    }

    public void checkAvailability(int bookId) throws BookNotAvailableException {
        Optional<Book> book = bookDAO.findById(bookId);
        if (book.isPresent() && book.get().getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No copies available for book ID: " + bookId);
        }
    }
}
