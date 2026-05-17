package com.gym.mapper;

import com.gym.dto.MemberDTO;
import com.gym.pojo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {

    Member selectById(@Param("memberId") Integer memberId);

    Member selectByPhone(@Param("memberPhone") String memberPhone);

    List<Member> selectAll();

    List<Member> selectByKeyword(@Param("keyword") String keyword);

    List<Member> selectByRegex(@Param("field") String field,
                            @Param("operator") String operator,
                            @Param("numValue") Integer numValue,
                            @Param("likePattern") String likePattern,
                            @Param("startDate") String startDate,
                            @Param("endDate") String endDate);

    int insert(Member member);

    int update(Member member);

    int deleteById(@Param("memberId") Integer memberId);

    Integer selectTotalCount();

    List<MemberDTO> selectPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
}