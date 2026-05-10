package com.gym.service;

import com.gym.pojo.Member;

import java.util.List;

public interface MemberService {
    Integer selectTotalCount();

    Member selectById(Integer memberId);

    Member selectByPhone(Long memberPhone);

    List<Member> selectAll();

    List<Member> selectByKeyword(String keyword);

    int insert(Member member);

    int update(Member member);

    int delete(Integer memberId);
}