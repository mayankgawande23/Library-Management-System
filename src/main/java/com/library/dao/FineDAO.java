package com.library.dao;

import com.library.model.Fine;

import java.util.List;
import java.util.Optional;

public interface FineDAO {
    void addFine(Fine fine);
    List<Fine> getAllFines();
    List<Fine> getFinesForMember(int memberId);
    Optional<Fine> findByTransactionId(int transactionId);
    void markAsPaid(int fineId);
}
