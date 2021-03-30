package nl.stokpop.afterburner.mybatis;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@CacheNamespace(readWrite = false)
public interface EmployeeMapper {
    @Select("SELECT * FROM employees.employees WHERE first_name = #{firstName}")
    //@Transactional(timeout = 1)
    List<Employee> selectEmployeeByFirstName(@Param("firstName") String firstName);

    @Select("SELECT * FROM employees.employees WHERE last_name = #{lastName}")
    //@Transactional(timeout = 1)
    List<Employee> selectEmployeeByLastName(@Param("lastName") String lastName);

    @Select("SELECT * FROM employees.employees WHERE first_name = #{firstName} AND last_name = #{lastName}")
    //@Transactional(timeout = 1)
    List<Employee> selectEmployeeByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Select("SELECT SLEEP(#{durationInSec})")
    //@QueryHints(value = { @QueryHint(name = "javax.persistence.query.timeout", value = "20") })
    int selectLongTime(@Param("durationInSec") int durationInSec);

}
