package Models;

import Models.Employees.Employee;
import Models.Products.Product;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    public static int idCounter=1;

    int id;
    String location;
    List<Employee> employees= new ArrayList<>();
    List<Product> products= new ArrayList<>();
    List<Order> orders= new ArrayList<>();
}