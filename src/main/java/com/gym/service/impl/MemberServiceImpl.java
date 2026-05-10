package com.gym.service.impl;

import com.gym.mapper.MemberMapper;
import com.gym.pojo.Member;
import com.gym.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Integer selectTotalCount() {
        return memberMapper.selectTotalCount();
    }

    @Override
    public Member selectById(Integer memberId) {
        return memberMapper.selectById(memberId);
    }

    @Override
    public Member selectByPhone(Long memberPhone) {
        return memberMapper.selectByPhone(memberPhone);
    }

    @Override
    public List<Member> selectAll() {
        return memberMapper.selectAll();
    }

    @Override
    public List<Member> selectByKeyword(String keyword) {
        return memberMapper.selectByKeyword(keyword);
    }

    @Override
    public int insert(Member member) {
        return memberMapper.insert(member);
    }

    @Override
    public int update(Member member) {
        return memberMapper.update(member);
    }

    @Override
    public int delete(Integer memberId) {
        return memberMapper.deleteById(memberId);
    }
}