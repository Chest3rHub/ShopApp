package Models.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ProductWithSizeAndQtity {

    private Product product;
    private LinkedHashMap<Size,Integer> sizes;

    public static List<ProductWithSizeAndQtity> availableProductsWithSizesAndQtity= new ArrayList<>();

    public ProductWithSizeAndQtity(Product product) {
        this.product = product;
        this.sizes = new LinkedHashMap<>();
        availableProductsWithSizesAndQtity.add(this);
    }

    public void addSizeAndQuantity(Size size, int quantity) {
        sizes.put(size,quantity);
    }
    public String toString(){
        return this.product + " \nAvailable: " + sizes;
    }
    public static void showAllAvailableProductsWithSizesAndQtity(){
        for (ProductWithSizeAndQtity productWithSizeAndQtity : availableProductsWithSizesAndQtity){
            System.out.println(productWithSizeAndQtity.toString());
        }
    }
}
