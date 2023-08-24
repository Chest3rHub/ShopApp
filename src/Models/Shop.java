package Models;

// import Models.Employees.Employee;
import Exceptions.NoSuchProductException;
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
      //  Product p1= new Product(Category.PANTS,"Spodenki","Champion",149.99,"Przewiewne na lato");
      //  Product p2= new Product()
        int idDoPodaniaPrzyWyszukiwaniu=1;
        Optional<Integer> optionalInteger1 = Product.allProducts.stream()
                .filter(product -> product.getId()==idDoPodaniaPrzyWyszukiwaniu)
                .map(Product::getId)
                .findFirst();
        if (!optionalInteger1.isPresent()){
            throw new NoSuchProductException("No such product!");
        }
        int testId1= optionalInteger1.get();

        ProductWithSizeAndQtity productWithSizeAndQtity1= new ProductWithSizeAndQtity(Product.allProducts.get(testId1-1));
        productWithSizeAndQtity1.addSizeAndQuantity(Size.XS,5);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.S,10);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.M,15);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.L,20);
        productWithSizeAndQtity1.addSizeAndQuantity(Size.XL,25);

        productWithSizeAndQtity1.increaseProductQuantity(Size.M,15);
        int drugieIdDoPodania=2;
        Optional<Integer> optionalInteger2 = Product.allProducts.stream()
                .filter(product -> product.getId()==drugieIdDoPodania)
                .map(Product::getId)
                .findFirst();
        if (!optionalInteger2.isPresent()){
            throw new NoSuchProductException("No such product!");
        }
        int testId2= optionalInteger2.get();

        ProductWithSizeAndQtity productWithSizeAndQtity2= new ProductWithSizeAndQtity(Product.allProducts.get(testId2-1));

        productWithSizeAndQtity2.addSizeAndQuantity(Size.XS,7);
        productWithSizeAndQtity2.addSizeAndQuantity(Size.M,2);
        productWithSizeAndQtity2.addSizeAndQuantity(Size.L,11);

        productWithSizeAndQtity2.decreaseProductQuantity(Size.XS,7);
       // productWithSizeAndQtity2.decreaseQuantityAvailable(Size.M,2);
       // productWithSizeAndQtity2.decreaseQuantityAvailable(Size.L,11);

        int trzecieIdDoPodania=3;
        Optional<Integer> optionalInteger3 = Product.allProducts.stream()
                .filter(product -> product.getId()==trzecieIdDoPodania)
                .map(Product::getId)
                .findFirst();
        if (!optionalInteger3.isPresent()){
            throw new NoSuchProductException("No such product!");
        }
        int testId3= optionalInteger3.get();

        ProductWithSizeAndQtity productWithSizeAndQtity3= new ProductWithSizeAndQtity(Product.allProducts.get(testId3-1));

        productWithSizeAndQtity3.addSizeAndQuantity(Size.XS,1);
        productWithSizeAndQtity3.addSizeAndQuantity(Size.S,3);
        productWithSizeAndQtity3.addSizeAndQuantity(Size.M,8);
        productWithSizeAndQtity3.addSizeAndQuantity(Size.L,2);

        int czwarteIdDoPodania=5;
        Optional<Integer> optionalInteger4 = Product.allProducts.stream()
                .filter(product -> product.getId()==czwarteIdDoPodania)
                .map(Product::getId)
                .findFirst();
        if (!optionalInteger4.isPresent()){
            throw new NoSuchProductException("No such product!");
        }
        int testId4= optionalInteger4.get();

        ProductWithSizeAndQtity productWithSizeAndQtity4= new ProductWithSizeAndQtity(Product.allProducts.get(testId4-1));
        productWithSizeAndQtity4.addSizeAndQuantity(Size.M,14);

        int piateIdDoPodania=6;
        Optional<Integer> optionalInteger5 = Product.allProducts.stream()
                .filter(product -> product.getId()==piateIdDoPodania)
                .map(Product::getId)
                .findFirst();
        if (!optionalInteger5.isPresent()){
            throw new NoSuchProductException("No such product!");
        }
        int testId5= optionalInteger5.get();

        ProductWithSizeAndQtity productWithSizeAndQtity5= new ProductWithSizeAndQtity(Product.allProducts.get(testId5-1));
        productWithSizeAndQtity5.addSizeAndQuantity(Size.L,3);

        System.out.println("====");
        System.out.println("Wyswietlanie wszystkich dostepnych za pomoca metody showAllAvailableProductsWithSizesAndQtity");
        ProductWithSizeAndQtity.showAllAvailableProductsWithSizesAndQtity();
        System.out.println("Koniec wyswietlania wszystkich");
        System.out.println("====");


        System.out.println("Rozpoczecie metody wyswietlania z danej kategorii");
        Category category= Category.HOODIE;
        List<ProductWithSizeAndQtity> categorySearchingList= ProductWithSizeAndQtity.getProductListByCategory(category,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(categorySearchingList);
        System.out.println("Zakonczenie metody z danej kategorii");

        System.out.println("=====");
        System.out.println("Wyswietlanie posortowanych po cenie rosnaco");
        List<ProductWithSizeAndQtity> orderedByPriceAscList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(orderedByPriceAscList);
        System.out.println("koniec wyswietlania rosnaco po cenie");
        System.out.println("======");

        System.out.println("=====");
        System.out.println("Wyswietlanie posortowanych po cenie malejaco");
        List<ProductWithSizeAndQtity> orderedByPriceDescList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(orderedByPriceDescList);
        System.out.println("koniec wyswietlania malejaco po cenie");
        System.out.println("======");


        System.out.println("====");
        System.out.println("Wyswietlanie posortowanych po cenie rosnaco z wybranej kategorii");
        Category category2=Category.HOODIE;
        List<ProductWithSizeAndQtity> sortedByPriceAscFromCategory= ProductWithSizeAndQtity.getProductListByCategoryOrderedByPriceAsc(category2,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(sortedByPriceAscFromCategory);

        System.out.println("Koniec wyswietlania po cenie rosnaco z wybranej kategorii");
        System.out.println("======");

        System.out.println("======");
        System.out.println("Wyswietlanie posortowanych po cenie malejaco z podanej kategorii");
        List<ProductWithSizeAndQtity> sortedByPriceDescFromCategory= ProductWithSizeAndQtity.getProductListByCategoryOrderedByPriceDesc(category2,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(sortedByPriceDescFromCategory);
        System.out.println("Koniec wyswietlania z kategorii posortowanych malejaco");
        System.out.println("======");

        System.out.println("=====");
        System.out.println("Rozpoczecie metody szukania po nazwie produktu");
        String nazwa1="bluza";
        List<ProductWithSizeAndQtity> productNameSearchingList= ProductWithSizeAndQtity.getProductListByName(nazwa1,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(productNameSearchingList);
        System.out.println("Zakonczenie metody szukania po nazwie produktu");
        System.out.println("=====");


        System.out.println("====");
        System.out.println("Rozpoczecie metody szukania produktow o podanym rozmiarze");
        Size size1= Size.M;
        List<ProductWithSizeAndQtity> sizeSearchingList= ProductWithSizeAndQtity.getProductListBySize(size1,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(sizeSearchingList);
        System.out.println("Zakonczenie szukania produktow o podanym rozmiarze ");
        System.out.println("====");

        System.out.println("====");
        System.out.println("Rozpoczecie metody szukania produktow z podanego brandu:");
        String brand1="nike";
        List<ProductWithSizeAndQtity> brandSearchingList= ProductWithSizeAndQtity.getProductListByBrand(brand1,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(brandSearchingList);
        System.out.println("Zakonczenie metody szukania z danego brandu");
        System.out.println("=====");

        System.out.println("======");
        System.out.println("Wyszukiwanie z okreslonej kategorii i brandu");
        List<ProductWithSizeAndQtity> categoryAndBrandSearchingList=ProductWithSizeAndQtity.getProductListByCategoryAndBrand(category,brand1,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(categoryAndBrandSearchingList);
        System.out.println("Koniec wyszukiwania z okreslonej kategorii i brandu");
        System.out.println("====");

        System.out.println("======");
        System.out.println("Wyszukiwanie z okreslonego brandu i nazwy produktu");
        List<ProductWithSizeAndQtity> brandAndProductNameList=ProductWithSizeAndQtity.getProductListByBrandAndName(brand1,nazwa1,ProductWithSizeAndQtity.availableProductsWithSizesAndQtity);
        ProductWithSizeAndQtity.printProductsFromSelectedList(brandAndProductNameList);
        System.out.println("Koniec wyszukiwania z okreslonej kategorii i brandu");
        System.out.println("====");

    }
}