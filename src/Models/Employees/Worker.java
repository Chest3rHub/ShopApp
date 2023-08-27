package Models.Employees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Worker extends AbstractEmployee {

    List<Feedback> feedbackFromManagerList;

    public Worker(String firstName, String lastName, LocalDate hireDate, double salary, Role role){
        super(firstName,lastName,hireDate,salary,role);
        feedbackFromManagerList= new ArrayList<>();
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
