package io.perfana.afterburner.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@CacheNamespace(readWrite = false)
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

    @Select("SELECT e.* " +
            "FROM employees AS e " +
            "         JOIN ( " +
            "    SELECT emp_no " +
            "    FROM dept_emp AS de1 " +
            "    WHERE to_date = ( " +
            "        SELECT MAX(to_date) " +
            "        FROM dept_emp AS de2 " +
            "        WHERE de1.emp_no = de2.emp_no " +
            "    ) " +
            ") AS subq ON e.emp_no = subq.emp_no " +
            "         JOIN dept_emp AS de ON e.emp_no = de.emp_no " +
            "         JOIN departments AS d ON de.dept_no = d.dept_no " +
            "         JOIN salaries AS s ON e.emp_no = s.emp_no " +
            "WHERE d.dept_no IN ( " +
            "    SELECT dm.dept_no " +
            "    FROM dept_manager dm " +
            "             JOIN employees em ON dm.emp_no = em.emp_no" +
            "    WHERE em.last_name = #{lastName}" +
            "      AND dm.to_date = (SELECT MAX(to_date) FROM dept_manager WHERE dept_no = dm.dept_no) " +
            "      AND s.to_date = (SELECT MAX(to_date) FROM salaries WHERE emp_no = em.emp_no)) limit 200;"
    )
    List<Employee> findEmployeesByManagerLastName(@Param("lastName") String lastName);

}
