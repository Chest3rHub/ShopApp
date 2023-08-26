package Models;

import Models.Products.Product;
import Models.Products.ProductWithSizeAndQtity;

import java.util.ArrayList;
import java.util.List;

public class Order {
    int id;
   // List<Product> products= new ArrayList<>();
    // raczej lista product with size and qtity
    List<ProductWithSizeAndQtity> orderedProducts= new ArrayList<>();
    double totalCost;

    public String toString(){
        return this.id + ";"
                + this.orderedProducts + ";"
                + this.totalCost;

        // moze zmienic sredniki na cos innego bo jakbym chcial skanowac po sredniku to przedmioty w klasie
        // productwithsizeandqtity sa odzielone chyba srednikami
    }
}