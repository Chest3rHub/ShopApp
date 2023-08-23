package Models.Products;

import Exceptions.NotEnoughProductsException;

import java.util.*;
import java.util.stream.Collectors;

public class ProductWithSizeAndQtity {

    private Product product;
    private LinkedHashMap<Size,Integer> sizesAndQuantitiesMap;

    public static List<ProductWithSizeAndQtity> availableProductsWithSizesAndQtity= new ArrayList<>();

    public ProductWithSizeAndQtity(Product product) {
        this.product = product;
        this.sizesAndQuantitiesMap = new LinkedHashMap<>();
        availableProductsWithSizesAndQtity.add(this);
    }

    public void addSizeAndQuantity(Size size, int quantity) {
        sizesAndQuantitiesMap.put(size,quantity);
    }
    public String toString(){
        return this.product + " \nAvailable: " + sizesAndQuantitiesMap;
    }
    public static void showAllAvailableProductsWithSizesAndQtity(){
        for (ProductWithSizeAndQtity productWithSizeAndQtity : availableProductsWithSizesAndQtity){
            System.out.println(productWithSizeAndQtity.toString());
        }
    }
    public static void showByCategoryAvailableProductsWithSizesAndQtity(Category category){
        System.out.println("Displaying available products from the selected category: ");
        for (ProductWithSizeAndQtity productWithSizeAndQtity : availableProductsWithSizesAndQtity){
            if (productWithSizeAndQtity.product.category.equals(category)){
                System.out.println(productWithSizeAndQtity.toString());
            }
        }
    }
    public static void orderByPriceAscendingAvailableProductsWithSizesAndQtity(){
        System.out.println("Product ordered by price ascending: ");
       List<ProductWithSizeAndQtity> orderedByAscending = availableProductsWithSizesAndQtity
               .stream()
               .sorted(Comparator.comparing(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getPrice()))
               .collect(Collectors.toList());

        for (ProductWithSizeAndQtity productWithSizeAndQtity : orderedByAscending){
            System.out.println(productWithSizeAndQtity.toString());
        }
    }

    public static void orderByPriceDescendingAvailableProductsWithSizesAndQtity(){
        System.out.println("Product ordered by price descending: ");
        List<ProductWithSizeAndQtity> orderedByAscending = availableProductsWithSizesAndQtity
                .stream()
                .sorted(Comparator.comparing(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getPrice()))
                .collect(Collectors.toList());
        List<ProductWithSizeAndQtity> orderedByDescending = new LinkedList<>();
        for (ProductWithSizeAndQtity productWithSizeAndQtity : orderedByAscending) {
            orderedByDescending.add(0, productWithSizeAndQtity);
        }
        for (ProductWithSizeAndQtity productWithSizeAndQtity : orderedByDescending){
            System.out.println(productWithSizeAndQtity.toString());
        }
    }

    // dodac jeszcze orderedByPrice ale z wybranej kategorii

    public void increaseQuantityAvailable(Size size, int increaseAmount){
         int currentQuantity= this.sizesAndQuantitiesMap.get(size);
         int newQuantity= currentQuantity + increaseAmount;
         this.sizesAndQuantitiesMap.replace(size,newQuantity);
    }
    public void decreaseQuantityAvailable(Size size, int decreaseAmount) throws NotEnoughProductsException {
        int currentQuantity= this.sizesAndQuantitiesMap.get(size);
        if (currentQuantity<decreaseAmount){
            throw new NotEnoughProductsException("There are not enough products to reduce their quantity by this amount!");
        }
        int newQuantity= currentQuantity - decreaseAmount;
        if (newQuantity==0){
            this.sizesAndQuantitiesMap.remove(size);
        }else {
            this.sizesAndQuantitiesMap.replace(size,newQuantity);
        }
        // dodac walidacje przy zakupie ktora sprawi ze nie da sie odjac wiecej niz obecny amount
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LinkedHashMap<Size, Integer> getSizesAndQuantitiesMap() {
        return sizesAndQuantitiesMap;
    }

    public void setSizesAndQuantitiesMap(LinkedHashMap<Size, Integer> sizesAndQuantitiesMap) {
        this.sizesAndQuantitiesMap = sizesAndQuantitiesMap;
    }
}
