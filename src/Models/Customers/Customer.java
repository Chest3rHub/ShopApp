package Models.Customers;

import Models.Employees.Role;
import Models.Order;

import java.util.ArrayList;
import java.util.List;

public class Customer {
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

}