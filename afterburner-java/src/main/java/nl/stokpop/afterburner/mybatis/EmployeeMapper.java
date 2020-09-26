package nl.stokpop.afterburner.mybatis;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@CacheNamespace(readWrite = false)
public interface EmployeeMapper {
    @Select("SELECT * FROM employees.employees WHERE first_name = #{firstName}")
    List<Employee> selectEmployeeWithFirstName(@Param("firstName") String firstName);
}
