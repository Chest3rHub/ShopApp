package Models.Customers;

import DTOs.ProductInCartDTO;
import Interfaces.IPersonInfo;
import Models.Employees.Consultant;
import Models.Employees.Feedback;
import Models.Employees.Role;
import Models.Order;
import Models.Products.ProductWithSizeAndQtity;

import javax.swing.text.html.Option;
import java.text.DecimalFormat;
import java.util.*;

public class Customer implements IPersonInfo {
    public static HashMap<String, Customer> customers= new HashMap<>();
    public static int idCounter=1;



    int id;
    String login;
    String password;
    String firstName;
    String lastName;
    // moze podzielic na kod pocztowy ulice miasto itd...
    String address;
    int tel;

    double credits;
    String email;
    Role role;
    List<Integer> ordersIds = new ArrayList<>();
    List<ProductInCartDTO> currentCart= new ArrayList<>();
    List<Order> orders= new ArrayList<>();

    // password dodac, metody register, login, hashowanie hasel itd;

    public Customer(String login, String password){
        this.login=login;
        this.password=password;
        this.role=Role.CLIENT;
        customers.put(login,this);
        this.credits=0;
    }

    public Customer(String login, String firstName, String lastName, String address, int tel, double credits, String email, List<ProductInCartDTO> currentCart, List<Integer> ordersIds) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.tel = tel;
        this.credits = credits;
        this.email = email;
        this.ordersIds = ordersIds;
        this.currentCart = currentCart;
    }
    public static void addOrdersToCustomersStartUp(){
        // na poczatku dodac odpowiednim klientom zamowienia o takich id jakie sa w ich liscie ordersIds
        for (Map.Entry<String, Customer> customerFromMap : customers.entrySet()) {
            String loginHash = customerFromMap.getKey();
            Customer customer = customerFromMap.getValue();
            List<Integer> ordersIdsTemp=customer.getOrdersIds();
            System.out.println("Dodawanie zamowien do klientow: "+ ordersIdsTemp);
            if (ordersIdsTemp.isEmpty()){
                continue;
            }
            for (int id : ordersIdsTemp){
                Optional<Order> orderTemp = Order.allOrders.stream()
                        .filter(order -> order.getIdOrder()==id)
                        .findFirst();
                if (!orderTemp.isPresent()){
                    continue;
                }
                Order orderToAdd= orderTemp.get();
                customer.orders.add(orderToAdd);
            }
        }
    }

    public void addCredits(Double credits){
        this.credits+=credits;
    }
    public void addCredits(String creditsString) throws IllegalArgumentException{
        int amount= Integer.parseInt(creditsString);
        this.credits+=amount;
    }
    public void addToCart(ProductInCartDTO productWithSizeAndQtity){
        this.currentCart.add(productWithSizeAndQtity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getPersonalData(){
        return this.id + ";"
                + this.firstName + ";"
                + this.lastName + ";"
                + this.address + ";"
                + this.tel + ";"
                + this.email + ";"
                + this.role + ";"
                + this.ordersIds;

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

    public List<ProductInCartDTO> getCurrentCart() {
        return currentCart;
    }

    public void setCurrentCart(List<ProductInCartDTO> currentCart) {
        this.currentCart = currentCart;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static HashMap<String, Customer> getCustomers() {
        return customers;
    }

    public static void setCustomers(HashMap<String, Customer> customers) {
        Customer.customers = customers;
    }

    public List<Integer> getOrdersIds() {
        return ordersIds;
    }

    public void setOrdersIds(List<Integer> ordersIds) {
        this.ordersIds = ordersIds;
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
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        String formattedCredits = decimalFormat.format(credits);
        String noComma= formattedCredits.replace(",",".");
        return Double.parseDouble(noComma);
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