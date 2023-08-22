package Models;

import Models.Products.Product;

import java.util.ArrayList;
import java.util.List;

public class Order {
    int id;
    List<Product> products= new ArrayList<>();
    double totalCost;
}