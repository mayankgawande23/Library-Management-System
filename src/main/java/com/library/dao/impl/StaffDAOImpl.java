package com.library.dao.impl;

import com.library.config.DatabaseConfig;
import com.library.dao.StaffDAO;
import com.library.model.Staff;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StaffDAOImpl implements StaffDAO {

    @Override
    public Optional<Staff> findByEmail(String email) {
        String sql = "SELECT * FROM staff WHERE email=?";
        try (Connection connection = DatabaseConfig.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
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
    public boolean validateLogin(String email, String password) {
        Optional<Staff> staff = findByEmail(email);
//        return staff.isPresent() && BCrypt.checkpw(password, staff.get().getPasswordHash())
        return staff.isPresent() && staff.get().getPasswordHash().equals(password);
    }

    private Staff mapRow(ResultSet rs) throws SQLException {
        return new Staff(
                rs.getInt("staff_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role")
        );
    }
}
