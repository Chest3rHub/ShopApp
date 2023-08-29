package Models.Customers;

import Interfaces.IPersonInfo;
import Models.Employees.Consultant;
import Models.Employees.Feedback;
import Models.Employees.Role;
import Models.Order;

import java.util.ArrayList;
import java.util.List;

public class Customer implements IPersonInfo {
    public static List<Customer> customers= new ArrayList<>();
    public static int idCounter=1;



    int id;
    String login;
    String password;
    String firstName;
    String lastName;
    String address;
    int tel;
    String email;
    Role role;
    List<Order> orders= new ArrayList<>();

    // password dodac, metody register, login, hashowanie hasel itd;

    public Customer(String login, String password){
        this.login=login;
        this.password=password;
        this.role=Role.CLIENT;
        customers.add(this);
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}