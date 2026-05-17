package com.gym.service;

import com.gym.dto.MemberDTO;
import com.gym.dto.PageResult;
import com.gym.pojo.Member;

import java.util.List;

public interface MemberService {
    Integer selectTotalCount();

    Member selectById(Integer memberId);

    Member selectByPhone(String memberPhone);

    List<Member> selectAll();

    List<Member> selectByKeyword(String keyword);

    List<Member> selectByRegex(String field, String value, String operator, Integer numValue, String likePattern);

    PageResult<MemberDTO> getMemberPage(int pageNum, int pageSize);

    int insert(Member member);

    int update(Member member);

    int delete(Integer memberId);
}