package com.library.dao;

import com.library.model.Staff;

import java.util.Optional;

public interface StaffDAO {
    Optional<Staff> findByEmail(String email);
    boolean validateLogin(String email, String password);
}
