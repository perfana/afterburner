package io.perfana.afterburner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.perfana.afterburner.AfterburnerProperties;
import io.perfana.afterburner.domain.BurnerMessage;
import io.perfana.afterburner.mybatis.Employee;
import io.perfana.afterburner.mybatis.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@Profile("employee-db")
public class DatabaseConnector {

    private final JdbcTemplate template;

    private final AfterburnerProperties props;

    private final EmployeeMapper employeeMapper;

    @Autowired
    public DatabaseConnector(JdbcTemplate template, AfterburnerProperties props, EmployeeMapper employeeMapper) {
        this.template = template;
        this.props = props;
        this.employeeMapper = employeeMapper;
    }

    @Operation(summary = "Execute trivial query on remote database and measure the response time.")
    @GetMapping("/db/connect")
    public BurnerMessage checkDatabaseConnection() {
        long startTime = System.currentTimeMillis();

        String query = props.getDatabaseConnectQuery();

        long nanoStartTime = System.nanoTime();
        template.execute(query);
        long estimatedQueryTime = System.nanoTime() - nanoStartTime;

        long durationMillis = System.currentTimeMillis() - startTime;

        String message = String.format("{ 'db-call':'success','query-duration-nanos':%d }", estimatedQueryTime);

        return new BurnerMessage(message, props.getName(), durationMillis);
    }

    @Operation(summary = "Find employees by name.")
    @GetMapping("/db/employee/find-by-name")
    public List<Employee> findEmployeeByFirstName(
        @RequestParam(required = false, defaultValue = "") String firstName,
        @RequestParam(required = false, defaultValue = "") String lastName) {

        if (firstName.length() == 0 && lastName.length() > 0) {
            return employeeMapper.selectEmployeeByLastName(lastName);
        }
        else if (firstName.length() > 0 && lastName.length() == 0) {
            return employeeMapper.selectEmployeeByFirstName(firstName);
        }
        else if (firstName.length() > 0) {
            return employeeMapper.selectEmployeeByFirstAndLastName(firstName, lastName);
        }
        else {
            return Collections.emptyList();
        }
    }

    @Operation(description = "Find employees by last name.")
    @GetMapping("/db/employee/find-by-last-name")
    public List<Employee> findEmployeeByLastName(@RequestParam(defaultValue = "") String lastName) {
        return employeeMapper.selectEmployeeByLastName(lastName);
    }

    @Operation(description = "Execute long query.")
    @GetMapping("/db/employee/select-long-time")
    public int longQuery(@RequestParam int durationInSec) {
        return employeeMapper.selectLongTime(durationInSec);
    }
}
