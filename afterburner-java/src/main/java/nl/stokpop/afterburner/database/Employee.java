package nl.stokpop.afterburner.database;

import lombok.Value;

import java.time.LocalDate;

@Value
public class Employee {
    int empNo;
    LocalDate birthDate;
    String firstName;
    String lastName;
    Gender gender;
    LocalDate hireDate;
}
