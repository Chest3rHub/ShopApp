package Models.Employees;

import java.time.LocalDate;

public class Worker extends AbstractEmployee {

    public Worker(String firstName, String lastName, LocalDate hireDate, double salary, Role role){
        super(firstName,lastName,hireDate,salary,role);
    }
    @Override
    public String getPersonalData() {

        return this.id + ";"
                + this.firstName + ";"
                + this.lastName + ";"
                + this. hireDate + ";"
                + this.salary + ";"
                + this.role;
    }
}
