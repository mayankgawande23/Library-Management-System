package com.library.service;

import com.library.dao.MemberDAO;
import com.library.dao.TransactionDAO;
import com.library.dao.impl.MemberDAOImpl;
import com.library.dao.impl.TransactionDAOImpl;
import com.library.exception.MemberNotFoundException;
import com.library.model.Member;
import com.library.model.Transaction;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberDAO memberDAO;
    private final TransactionDAO transactionDAO;

    public MemberService() {
        this.memberDAO = new MemberDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
    }

    public void registerMember(Member member) {
        memberDAO.registerMember(member);
    }

    public void updateMember(Member member) {
        memberDAO.updateMember(member);
    }

    public void deactivateMember(int memberId) {
        memberDAO.deactivateMember(memberId);
    }

    public Member getMemberById(int memberId) throws MemberNotFoundException {
        return memberDAO.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found: " + memberId));
    }

    public Optional<Member> findByEmail(String email) {
        return memberDAO.findByEmail(email);
    }

    public List<Member> listActiveMembers() {
        return memberDAO.listActiveMembers();
    }

    public List<Transaction> getBorrowingHistory(int memberId) {
        return transactionDAO.getTransactionsByMember(memberId);
    }
}
