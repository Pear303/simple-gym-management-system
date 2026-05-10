package com.gym.mapper;

import com.gym.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<Employee> selectAll();

    Employee selectById(@Param("employeeId") Integer employeeId);

    List<Employee> selectByRegex(@Param("field") String field, 
                            @Param("operator") String operator, 
                            @Param("numValue") Integer numValue, 
                            @Param("likePattern") String likePattern);

    int insert(Employee employee);

    int update(Employee employee);

    int deleteById(@Param("employeeId") Integer employeeId);

    int selectTotalCount();

}