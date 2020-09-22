package nl.stokpop.afterburner.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    @Select("SELECT * FROM employees.employees WHERE first_name = #{firstName}")
    List<Employee> selectEmployeeWithFirstName(@Param("firstName") String firstName);
}
