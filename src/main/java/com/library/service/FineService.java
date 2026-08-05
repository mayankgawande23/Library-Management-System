package com.library.service;

import com.library.dao.FineDAO;
import com.library.dao.impl.FineDAOImpl;
import com.library.model.Fine;

import java.util.List;

public class FineService {
    private final FineDAO fineDAO;

    public FineService() {
        this.fineDAO = new FineDAOImpl();
    }

    public List<Fine> getAllFines() {
        return fineDAO.getAllFines();
    }

    public List<Fine> getFinesForMember(int memberId) {
        return fineDAO.getFinesForMember(memberId);
    }

    public void markFineAsPaid(int fineId) {
        fineDAO.markAsPaid(fineId);
    }
}
