package Models;

// import Models.Employees.Employee;
import Models.Products.Category;
import Models.Products.Product;
import Models.Products.ProductWithSizeAndQtity;
import Models.Products.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Shop {
    public static int idCounter=1;

    int id;
    String location;
   // List<Employee> employees= new ArrayList<>();
    List<Product> products= new ArrayList<>();
    List<Order> orders= new ArrayList<>();
    Warehouse warehouse;

    public static void openShop(){
        Product.readProductsFromFile();
    }

    public static void closeShop(){

    }
    public static void seedData() throws Exception {
        Product p1= new Product(Category.PANTS,"Spodenki","Champion",149.99,"Przewiewne na lato");
      //  Product p2= new Product()
        int idDoPodaniaPrzyWyszukiwaniu=1;
        Optional<Integer> optionalInteger1 = Product.allProducts.stream()
                .filter(product -> product.getId()==idDoPodaniaPrzyWyszukiwaniu)
                .map(Product::getId)
                .findFirst();
        if (!optionalInteger1.isPresent()){
            throw new Exception("No such product!");
        }
        int testId1= optionalInteger1.get();

        ProductWithSizeAndQtity productWithSizeAndQtity1= new ProductWithSizeAndQtity(Product.allProducts.get(testId1-1));
        productWithSizeAndQtity1.addSizeAndQuantity(Size.XS,5);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.S,10);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.M,15);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.L,20);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.XL,25);


        int drugieIdDoPodania=2;
        Optional<Integer> optionalInteger2 = Product.allProducts.stream()
                .filter(product -> product.getId()==drugieIdDoPodania)
                .map(Product::getId)
                .findFirst();
        if (!optionalInteger2.isPresent()){
            throw new Exception("No such product!");
        }
        int testId2= optionalInteger2.get();

        ProductWithSizeAndQtity productWithSizeAndQtity2= new ProductWithSizeAndQtity(Product.allProducts.get(testId2-1));

        productWithSizeAndQtity2.addSizeAndQuantity(Size.XS,7);
        productWithSizeAndQtity2.addSizeAndQuantity(Size.M,2);
        productWithSizeAndQtity2.addSizeAndQuantity(Size.L,11);


        ProductWithSizeAndQtity.showAllAvailableProductsWithSizesAndQtity();
    }
}