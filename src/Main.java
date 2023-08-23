import Models.Products.Product;
import Models.Shop;

public class Main {

    public static void main(String[] args) {
        Shop.openShop();
        Product.showProducts();
        System.out.println("======");
        try {
            Shop.seedData();
        }catch (Exception e){
            e.printStackTrace();
           // e.getMessage();
        }
     //   Product.addProduct();
     //   Product.addProduct();
     //   Product.showProducts();
     //   Product.saveProductsToFile();
    }
}