package com.library.dao;

import com.library.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionDAO {
    void issueBook(Transaction transaction);
    void returnBook(int transactionId, Transaction transaction);
    Optional<Transaction> findById(int transactionId);
    Optional<Transaction> findActiveTransaction(int bookId, int memberId);
    List<Transaction> getTransactionsByMember(int memberId);
    List<Transaction> getAllTransactions();
    List<Transaction> getOverdueTransactions();
    List<Transaction> getMostBorrowedBooks();
}
