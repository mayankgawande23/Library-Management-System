package com.library.dao.impl;

import com.library.config.DatabaseConfig;
import com.library.dao.FineDAO;
import com.library.model.Fine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FineDAOImpl implements FineDAO {

    @Override
    public void addFine(Fine fine) {
        String sql = "INSERT INTO fines (transaction_id, member_id, amount, paid_status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fine.getTransactionId());
            statement.setInt(2, fine.getMemberId());
            statement.setBigDecimal(3, fine.getAmount());
            statement.setBoolean(4, fine.isPaidStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Fine> getAllFines() {
        String sql = "SELECT * FROM fines ORDER BY fine_id";
        List<Fine> fines = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                fines.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fines;
    }

    @Override
    public List<Fine> getFinesForMember(int memberId) {
        String sql = "SELECT * FROM fines WHERE member_id=? ORDER BY fine_id";
        List<Fine> fines = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                fines.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fines;
    }

    @Override
    public Optional<Fine> findByTransactionId(int transactionId) {
        String sql = "SELECT * FROM fines WHERE transaction_id=?";
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
    public void markAsPaid(int fineId) {
        String sql = "UPDATE fines SET paid_status=true, paid_date=NOW() WHERE fine_id=?";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fineId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Fine mapRow(ResultSet rs) throws SQLException {
        return new Fine(
                rs.getInt("fine_id"),
                rs.getInt("transaction_id"),
                rs.getInt("member_id"),
                rs.getBigDecimal("amount"),
                rs.getBoolean("paid_status"),
                rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null
        );
    }
}
