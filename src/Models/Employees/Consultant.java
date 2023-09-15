package Models.Employees;

import Models.Order;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Consultant extends AbstractEmployee {
    public static final String _consultantFeedbackFileName="src/Data/FeedbackConsultants.txt";

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

    public List<Feedback> getFeedbackFromCustomerList() {
        return feedbackFromCustomerList;
    }

    public void setFeedbackFromCustomerList(List<Feedback> feedbackFromCustomerList) {
        this.feedbackFromCustomerList = feedbackFromCustomerList;
    }

    public static Consultant getRandomConsultant(){
        Random rand = new Random();
        int randomIndex = rand.nextInt(consultantList.size());

        return consultantList.get(randomIndex);
    }
    public static void saveConsultantFeedbackToFile(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(_consultantFeedbackFileName));
            bufferedWriter.write("ConsultantId;Rating;LocaleDate;String comment\n");
            for (Consultant consultant : consultantList) {
                List<Feedback> tempList= consultant.getFeedbackFromCustomerList();
                for (Feedback feedback : tempList){
                    bufferedWriter.write(consultant.getId() + ";"
                    + feedback.getRating() + ";"
                    + feedback.getDate() + ";"
                    + feedback.getComment());
                    bufferedWriter.write("\n");
                }

            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
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
