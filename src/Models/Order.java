package Models;

import DTOs.ProductInCartDTO;
import Models.Products.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    public static List<Order> allOrders= new ArrayList<>();
    public static double totalRevenue=0;
    public static int idCounter=1;
    int idOrder;
    //String loginHash;
   // List<Product> products= new ArrayList<>();
    // raczej lista product with size and qtity

    // chyba zmienic na ProductDTO
    // moze dodac date zlozenia zamowienia i statyczna liste wszystkich zamowien
    LocalDate orderedAt;
    List<ProductInCartDTO> orderedProducts= new ArrayList<DTOs.ProductInCartDTO>();
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

    public double calculateCost(){
        double cost=0;
        for (ProductInCartDTO product : orderedProducts){
            int idProduct=product.getIdProduct();
            double itemCost = Product.allProducts.get(idProduct).getPrice();
            cost+=product.getQuantity() * itemCost;
        }
        return cost;
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