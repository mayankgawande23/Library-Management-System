package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.FineDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.impl.BookDAOImpl;
import com.library.dao.impl.FineDAOImpl;
import com.library.dao.impl.TransactionDAOImpl;
import com.library.exception.BookNotAvailableException;
import com.library.exception.MemberNotFoundException;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final BookDAO bookDAO;
    private final FineDAO fineDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.bookDAO = new BookDAOImpl();
        this.fineDAO = new FineDAOImpl();
    }

    public void issueBook(int bookId, int memberId) throws BookNotAvailableException, MemberNotFoundException {
        Optional<Book> bookOpt = bookDAO.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new MemberNotFoundException("Book not found");
        }
        Book book = bookOpt.get();
        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("Book is not available for issue");
        }

        Transaction transaction = new Transaction();
        transaction.setBookId(bookId);
        transaction.setMemberId(memberId);
        transaction.setDueDate(LocalDateTime.now().plusDays(14));
        transaction.setStatus("ISSUED");
        transactionDAO.issueBook(transaction);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookDAO.updateBook(book);
    }

    public void returnBook(int transactionId) {
        Optional<Transaction> transactionOpt = transactionDAO.findById(transactionId);
        if (transactionOpt.isEmpty()) {
            return;
        }

        Transaction transaction = transactionOpt.get();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal fine = BigDecimal.ZERO;
        if (transaction.getDueDate() != null && now.isAfter(transaction.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(transaction.getDueDate(), now);
            fine = BigDecimal.valueOf(daysLate * 10L);
        }
        transaction.setReturnDate(now);
        transaction.setFineAmount(fine);
        transaction.setStatus("RETURNED");
        transactionDAO.returnBook(transactionId, transaction);

        Optional<Book> book = bookDAO.findById(transaction.getBookId());
        if (book.isPresent()) {
            book.get().setAvailableCopies(book.get().getAvailableCopies() + 1);
            bookDAO.updateBook(book.get());
        }

        fineDAO.addFine(new Fine(0, transactionId, transaction.getMemberId(), fine, false, null));
    }

    public List<Transaction> getOverdueTransactions() {
        return transactionDAO.getOverdueTransactions();
    }

    public List<Transaction> getMostBorrowedBooks() {
        return transactionDAO.getMostBorrowedBooks();
    }
}
