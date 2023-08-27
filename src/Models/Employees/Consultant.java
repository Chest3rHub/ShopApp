package Models.Employees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Consultant extends AbstractEmployee {

    public Consultant(String firstName, String lastName, LocalDate hireDate, double salary, Role role){
        super(firstName,lastName,hireDate,salary,role);
    }

    List<Feedback> feedbackFromCustomerList = new ArrayList<>();

    public static List<Consultant> consultantList= new ArrayList<>();


    public void addFeedback(Feedback feedback){
        this.feedbackFromCustomerList.add(feedback);

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
