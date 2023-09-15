package Models.Employees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Consultant extends AbstractEmployee {

    List<Feedback> feedbackFromManagerList;
    List<Feedback> feedbackFromCustomerList;
    public static List<Consultant> consultantList= new ArrayList<>();

    public Consultant(String firstName, String lastName, LocalDate hireDate, double salary, Role role){
        super(firstName,lastName,hireDate,salary,role);
        feedbackFromManagerList= new ArrayList<>();
        feedbackFromCustomerList= new ArrayList<>();
        consultantList.add(this);
    }




    public void addFeedback(Feedback feedback){
        this.feedbackFromCustomerList.add(feedback);

    }

    public static Consultant getRandomConsultant(){
        Random rand = new Random();
        int randomIndex = rand.nextInt(consultantList.size());

        return consultantList.get(randomIndex);
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
