package com.library.service;

import com.library.dao.StaffDAO;
import com.library.dao.impl.StaffDAOImpl;
import com.library.exception.InvalidLoginException;
import com.library.model.Staff;

import java.util.Optional;

public class StaffService {
    private final StaffDAO staffDAO;

    public StaffService() {
        this.staffDAO = new StaffDAOImpl();
    }

    public Staff login(String email, String password) throws InvalidLoginException {
        Optional<Staff> staff = staffDAO.findByEmail(email);
        System.out.println(staff.isEmpty());
        if (staff.isEmpty() || !staffDAO.validateLogin(email, password)) {
            throw new InvalidLoginException("Invalid login credentials");
        }
        return staff.get();
    }
}
