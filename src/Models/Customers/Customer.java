package Models.Customers;

import Interfaces.IPersonInfo;
import Models.Employees.Consultant;
import Models.Employees.Feedback;
import Models.Employees.Role;
import Models.Order;

import java.util.ArrayList;
import java.util.List;

public class Customer implements IPersonInfo {
    public static int idCounter=1;

    int id;
    String firstName;
    String lastName;
    String address;
    int tel;
    String email;
    Role role;
    List<Order> orders= new ArrayList<>();

    // password dodac, metody register, login, hashowanie hasel itd;

    public String getPersonalData(){
        return this.id + ";"
                + this.firstName + ";"
                + this.lastName + ";"
                + this.address + ";"
                + this.tel + ";"
                + this.email + ";"
                + this.role + ";"
                + this.orders;

    }
    public void giveFeedbackOnConsultant(Consultant consultant, Feedback feedback){
        consultant.addFeedback(feedback);


    }
}