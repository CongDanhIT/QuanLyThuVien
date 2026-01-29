package com.app.service;

import com.app.entity.Member;
import com.app.repository.MemberRepository;
import java.util.List;

public class MemberService {
    private MemberRepository memberRepo = new MemberRepository();

    public List<Member> getAllMembers() {
        return memberRepo.findAll();
    }

    public void saveOrUpdateMember(Member member) {
        memberRepo.saveOrUpdate(member);
    }

    public void deleteMember(Long id) {
        memberRepo.deleteById(id);
    }

    public Member getMemberById(Long id) {
        return memberRepo.findById(id);
    }
}