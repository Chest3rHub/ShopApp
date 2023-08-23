package Models.Employees;

import Interfaces.IEmployeeInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Employee implements IEmployeeInfo {
    public static int idCounter=1;
    int id;
    String firstName;
    String lastName;
    Date hireDate;
    double salary;
    Role role;

    public static List<Employee> employees= new ArrayList<>();
    public Employee(){}


    public Employee(String firstName, String lastName, Date hireDate, double salary, Role role) {
        this.id=idCounter;
        idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireDate = hireDate;
        this.salary = salary;
        this.role = role;
    }

    @Override
    public String returnPersonalData() {

        return this.id + ";"
                + this.firstName + ";"
                + this.lastName + ";"
                + this. hireDate + ";"
                + this.salary + ";"
                + this.role;
    }
}