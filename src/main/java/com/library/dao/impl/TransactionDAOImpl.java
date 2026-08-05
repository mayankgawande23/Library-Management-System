package com.library.dao.impl;

import com.library.config.DatabaseConfig;
import com.library.dao.TransactionDAO;
import com.library.model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public void issueBook(Transaction transaction) {
        String sql = "INSERT INTO transactions (book_id, member_id, due_date, status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getBookId());
            statement.setInt(2, transaction.getMemberId());
            statement.setTimestamp(3, Timestamp.valueOf(transaction.getDueDate()));
            statement.setString(4, transaction.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnBook(int transactionId, Transaction transaction) {
        String sql = "UPDATE transactions SET return_date=?, fine_amount=?, status=? WHERE transaction_id=?";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, Timestamp.valueOf(transaction.getReturnDate()));
            statement.setBigDecimal(2, transaction.getFineAmount());
            statement.setString(3, transaction.getStatus());
            statement.setInt(4, transactionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Transaction> findById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id=?";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transactionId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> findActiveTransaction(int bookId, int memberId) {
        String sql = "SELECT * FROM transactions WHERE book_id=? AND member_id=? AND status IN ('ISSUED', 'OVERDUE') ORDER BY transaction_id DESC LIMIT 1";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.setInt(2, memberId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Transaction> getTransactionsByMember(int memberId) {
        String sql = "SELECT * FROM transactions WHERE member_id=? ORDER BY issue_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                transactions.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions ORDER BY issue_date DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                transactions.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> getOverdueTransactions() {
        String sql = "SELECT * FROM transactions WHERE return_date IS NULL AND due_date < NOW()";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                transactions.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> getMostBorrowedBooks() {
        String sql = "SELECT book_id, COUNT(*) AS borrow_count FROM transactions GROUP BY book_id ORDER BY borrow_count DESC LIMIT 5";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setBookId(rs.getInt("book_id"));
                t.setFineAmount(BigDecimal.ZERO);
                transactions.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("transaction_id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                rs.getTimestamp("issue_date") != null ? rs.getTimestamp("issue_date").toLocalDateTime() : null,
                rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null,
                rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null,
                rs.getBigDecimal("fine_amount"),
                rs.getString("status")
        );
    }
}
