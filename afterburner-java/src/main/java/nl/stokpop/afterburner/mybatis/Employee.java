package nl.stokpop.afterburner.mybatis;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Value
public class Employee implements Serializable {
    int empNo;
    LocalDate birthDate;
    String firstName;
    String lastName;
    Gender gender;
    LocalDate hireDate;
}
