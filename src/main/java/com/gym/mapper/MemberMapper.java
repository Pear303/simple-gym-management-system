package com.gym.mapper;

import com.gym.pojo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    Member selectById(@Param("memberId") Integer memberId);

    Member selectByPhone(@Param("memberPhone") Long memberPhone);

    List<Member> selectAll();

    List<Member> selectByKeyword(@Param("keyword") String keyword);

    int insert(Member member);

    int update(Member member);

    int deleteById(@Param("memberId") Integer memberId);

    Integer selectTotalCount();
}