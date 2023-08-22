package Models.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductWithSizeAndQtity {

    private Product product;
    private List<ProductSize> sizes;

    public static List<ProductWithSizeAndQtity> availableProductsWithSizesAndQtity= new ArrayList<>();

    public ProductWithSizeAndQtity(Product product) {
        this.product = product;
        this.sizes = new ArrayList<>();
    }

    public void addSize(ProductSize size) {
        sizes.add(size);
    }
}
