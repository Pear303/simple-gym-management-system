package com.gym.mapper;

/*
 * mapper是数据访问层，负责MyBatis映射接口
 * 管理员Mapper
 */

import com.gym.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    // 根据账号查询管理员
    Admin selectByAccountAndPassword(Admin admin);

    // 查询所有管理员
    List<Admin> selectAll();

    // 新增管理员
    int insert(Admin admin);

    // 更新管理员密码
    int updatePassword(@Param("adminAccount") String adminAccount, @Param("newPassword") String newPassword);

    // 删除管理员
    int deleteByAccount(@Param("adminAccount") String adminAccount);
}
