package Models;

import DTOs.ProductInCartDTO;
import Models.Customers.Customer;
import Models.Products.Product;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order {
    public static List<Order> allOrders= new ArrayList<>();
    public final static String ordersFileName= "src/Data/Orders.txt";
    public static double totalRevenue=0;
    public static int idCounter=10531;
    int idOrder;
    //String loginHash;
   // List<Product> products= new ArrayList<>();
    // raczej lista product with size and qtity

    // chyba zmienic na ProductDTO
    // moze dodac date zlozenia zamowienia i statyczna liste wszystkich zamowien
    LocalDate orderedAt;
    List<ProductInCartDTO> orderedProducts;
    double totalCost;

    public Order(List<ProductInCartDTO> orderedProducts) {
        this.idOrder = idCounter;
       // this.loginHash = loginHash;
        this.orderedAt = LocalDate.now();
        this.orderedProducts = orderedProducts;
        this.totalCost = this.calculateCost();
        totalRevenue+=this.totalCost;
        allOrders.add(this);
        idCounter++;
    }
    public static void saveOrdersToFile(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ordersFileName));
            bufferedWriter.write("idOrder;orderDate;totalCost;orderedProducts\n");
            for (Order order : allOrders) {
                bufferedWriter.write(order.idOrder+"!"
                        + order.orderedAt +"!"
                        + order.totalCost + "!"
                        + order.orderedProducts);
                bufferedWriter.write("\n");
            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void readOrdersFromFile(){

    }



    public double calculateCost(){
        double cost=0;
        for (ProductInCartDTO product : orderedProducts){
            int idProduct=product.getIdProduct();
            double itemCost = Product.allProducts.get(idProduct).getPrice();
            cost+=product.getQuantity() * itemCost;
        }
        return cost;
    }

    public static List<Order> getAllOrders() {
        return allOrders;
    }

    public static void setAllOrders(List<Order> allOrders) {
        Order.allOrders = allOrders;
    }

    public static double getTotalRevenue() {
        return totalRevenue;
    }

    public static void setTotalRevenue(double totalRevenue) {
        Order.totalRevenue = totalRevenue;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Order.idCounter = idCounter;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public LocalDate getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDate orderedAt) {
        this.orderedAt = orderedAt;
    }

    public List<ProductInCartDTO> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<ProductInCartDTO> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String toString(){
        return this.idOrder + ";"
                + this.orderedAt + ";"
                + this.totalCost + ";"
                + this.orderedProducts;


        // moze zmienic sredniki na cos innego bo jakbym chcial skanowac po sredniku to przedmioty w klasie
        // productwithsizeandqtity sa odzielone chyba srednikami
    }
}