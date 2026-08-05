package com.library.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Fine {
    private int fineId;
    private int transactionId;
    private int memberId;
    private BigDecimal amount;
    private boolean paidStatus;
    private LocalDateTime paidDate;

    public Fine() {}

    public Fine(int fineId, int transactionId, int memberId, BigDecimal amount, boolean paidStatus, LocalDateTime paidDate) {
        this.fineId = fineId;
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.amount = amount;
        this.paidStatus = paidStatus;
        this.paidDate = paidDate;
    }

    public int getFineId() { return fineId; }
    public void setFineId(int fineId) { this.fineId = fineId; }
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public boolean isPaidStatus() { return paidStatus; }
    public void setPaidStatus(boolean paidStatus) { this.paidStatus = paidStatus; }
    public LocalDateTime getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDateTime paidDate) { this.paidDate = paidDate; }
}
