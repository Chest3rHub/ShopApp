package Models.Employees;

import java.util.ArrayList;
import java.util.List;

public class Consultant extends Employee{

    List<Feedback> feedbackFromCustomerList = new ArrayList<>();

    public static List<Consultant> consultantList= new ArrayList<>();


    public void addFeedback(Feedback feedback){
        this.feedbackFromCustomerList.add(feedback);

    }


    @Override
    public String getPersonalData() {
        return null;
    }
}
