package Models;

import Models.Products.ProductWithSizeAndQtity;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    int id;
    List<ProductWithSizeAndQtity> availableProducts= new ArrayList<>();
}
