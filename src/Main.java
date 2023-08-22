import Models.Products.Product;

public class Main {

    public static void main(String[] args) {
        Product.addProduct();
        Product.addProduct();
        Product.showProducts();
        Product.saveProductsToFile();
    }
}