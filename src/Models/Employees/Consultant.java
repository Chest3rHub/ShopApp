package Models.Employees;

import DTOs.ProductInCartDTO;
import Models.Order;
import Models.Products.Size;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class Consultant extends AbstractEmployee {
    public static final String _consultantFeedbackFileName="src/Data/FeedbackConsultants.txt";

    List<Feedback> feedbackFromManagerList;
    List<Feedback> feedbackFromCustomerList;
    public static List<Consultant> consultantList= new ArrayList<>();
    public double averageRating;


    public Consultant(String firstName, String lastName, LocalDate hireDate, double salary, Role role){
        super(firstName,lastName,hireDate,salary,role);
        feedbackFromManagerList= new ArrayList<>();
        feedbackFromCustomerList= new ArrayList<>();
        consultantList.add(this);
    }
    public static void readFeedbackFromFileAndAddToConsultants() throws IOException {
        /**
         * This method reads feedback from file and adds it to consultants.
         */
        try (BufferedReader br = new BufferedReader(new FileReader(_consultantFeedbackFileName))) {
            String line;
            line=br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                String idConsultantString = values[0];
                int idConsultant = Integer.parseInt(idConsultantString);
                String ratingString= values[1];
                Rating rating= Rating.valueOf(ratingString);
                String localDateString= values[2];
                LocalDate localDate= LocalDate.parse(localDateString);
                String comment=values[3];
                Feedback feedback= new Feedback(rating,comment,localDate);

                Optional<Consultant> optionalConsultant= consultantList.stream()
                        .filter(consultant -> consultant.getId()==idConsultant).findFirst();
                if (optionalConsultant.isPresent()){
                    Consultant consultant= optionalConsultant.get();
                    consultant.addFeedback(feedback);
                }
            }
        }
    }




    public void addFeedback(Feedback feedback){
        this.feedbackFromCustomerList.add(feedback);
    }
    public double calculateAverageRating(){
        double sum=0;
        for (Feedback feedback : feedbackFromCustomerList){
            if (feedback.getRating().equals(Rating.ONE)){
                sum+=1;
            } else if (feedback.getRating().equals(Rating.TWO)){
                sum+=2;
            } else if (feedback.getRating().equals(Rating.THREE)){
                sum+=3;
            } else if (feedback.getRating().equals(Rating.FOUR)){
                sum+=4;
            } else if (feedback.getRating().equals(Rating.FIVE)){
                sum+=5;
            }
        }
        double average= sum/feedbackFromCustomerList.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedRating = decimalFormat.format(average);
        String noCommaRating= formattedRating.replace(",",".");

        this.averageRating=Double.parseDouble(noCommaRating);
        return Double.parseDouble(noCommaRating);
    }

    public List<Feedback> getFeedbackFromCustomerList() {
        return feedbackFromCustomerList;
    }

    public void setFeedbackFromCustomerList(List<Feedback> feedbackFromCustomerList) {
        this.feedbackFromCustomerList = feedbackFromCustomerList;
    }

    public static Consultant getRandomConsultant(){
        /**
         * This method gets random Consultant from static consultantList
         * @return Random consultant
         */
        Random rand = new Random();
        int randomIndex = rand.nextInt(consultantList.size());

        return consultantList.get(randomIndex);
    }
    public static void saveConsultantFeedbackToFile(){
        /**
         * This method saves feedback about consultants to file
         */
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(_consultantFeedbackFileName));
            bufferedWriter.write("ConsultantId;Rating;LocalDate;String comment\n");
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
