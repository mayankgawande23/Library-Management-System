package com.library.dao;

import com.library.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberDAO {
    void registerMember(Member member);
    void updateMember(Member member);
    void deactivateMember(int memberId);
    Optional<Member> findById(int memberId);
    Optional<Member> findByEmail(String email);
    List<Member> listAllMembers();
    List<Member> listActiveMembers();
    List<Member> getBorrowingHistory(int memberId);
}
