package Models.Customers;

import Interfaces.IPersonInfo;
import Models.Employees.Consultant;
import Models.Employees.Feedback;
import Models.Employees.Role;
import Models.Order;
import Models.Products.ProductWithSizeAndQtity;

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

    double credits;
    String email;
    Role role;
    List<Order> orders= new ArrayList<>();
    List<ProductWithSizeAndQtity> currentCart= new ArrayList<>();

    // password dodac, metody register, login, hashowanie hasel itd;

    public Customer(String login, String password){
        this.login=login;
        this.password=password;
        this.role=Role.CLIENT;
        customers.add(this);
        this.credits=0;
    }
    public void addCredits(Double credits){
        this.credits+=credits;
    }
    public void addCredits(String creditsString) throws IllegalArgumentException{
        int amount= Integer.parseInt(creditsString);
        this.credits+=amount;
    }
    public void addToCart(ProductWithSizeAndQtity productWithSizeAndQtity){
        this.currentCart.add(productWithSizeAndQtity);
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

    public List<ProductWithSizeAndQtity> getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(List<ProductWithSizeAndQtity> currentCart) {
        this.currentCart = currentCart;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static List<Customer> getCustomers() {
        return customers;
    }

    public static void setCustomers(List<Customer> customers) {
        Customer.customers = customers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Customer.idCounter = idCounter;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}