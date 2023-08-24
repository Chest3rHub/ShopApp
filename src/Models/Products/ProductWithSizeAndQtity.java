package Models.Products;

import Exceptions.NotEnoughProductsException;
import Exceptions.UnavailableException;

import java.util.*;
import java.util.stream.Collectors;

public class ProductWithSizeAndQtity{

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
    public static List<ProductWithSizeAndQtity> getProductListByCategory(Category category, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Category -> " + category + ":");
        List<ProductWithSizeAndQtity> availableInCategory= parameterList
                .stream()
                .filter(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getCategory().equals(category))
                .toList();
        if (availableInCategory.isEmpty()){
            throw new UnavailableException("There are no available products from category " + category);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return availableInCategory;
    }
    public static List<ProductWithSizeAndQtity> getProductListOrderedByPriceAsc(List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Available products ordered by price ascending: ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ProductWithSizeAndQtity> orderedByAscending = parameterList
               .stream()
               .sorted(Comparator.comparing(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getPrice()))
               .collect(Collectors.toList());
       if (orderedByAscending.isEmpty()){
           throw new UnavailableException("There are no available products at the moment!");
       }
       return orderedByAscending;
    }

    public static List<ProductWithSizeAndQtity> getProductListOrderedByPriceDesc(List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Available products ordered by price descending: ");
        List<ProductWithSizeAndQtity> orderedByAscending = parameterList
                .stream()
                .sorted(Comparator.comparing(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getPrice()))
                .collect(Collectors.toList());
        if (orderedByAscending.isEmpty()){
            throw new UnavailableException("There are no available products at the moment!");
        }
        List<ProductWithSizeAndQtity> orderedByDescending = new LinkedList<>();
        for (ProductWithSizeAndQtity productWithSizeAndQtity : orderedByAscending) {
            orderedByDescending.add(0, productWithSizeAndQtity);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return orderedByDescending;
    }
    public static List<ProductWithSizeAndQtity> getProductListByCategoryOrderedByPriceAsc(Category category, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Displaying available products from category: " + category + " ordered by price ascending: ");
        List<ProductWithSizeAndQtity> selectedCategory= ProductWithSizeAndQtity.getProductListByCategory(category, parameterList);
        List<ProductWithSizeAndQtity> orderedByPriceAsc= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(selectedCategory);

        return orderedByPriceAsc;
    }
    public static List<ProductWithSizeAndQtity> getProductListByCategoryOrderedByPriceDesc(Category category, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Displaying available products from category: " + category + " ordered by price descending: ");
        List<ProductWithSizeAndQtity> selectedCategory=ProductWithSizeAndQtity.getProductListByCategory(category, parameterList);
        List<ProductWithSizeAndQtity> orderedByPriceDesc= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(selectedCategory);
        return orderedByPriceDesc;
    }


    public void increaseProductQuantity(Size size, int increaseAmount){
         int currentQuantity= this.sizesAndQuantitiesMap.get(size);
         int newQuantity= currentQuantity + increaseAmount;
         this.sizesAndQuantitiesMap.replace(size,newQuantity);
    }
    public void decreaseProductQuantity(Size size, int decreaseAmount) throws NotEnoughProductsException {
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
        if (this.sizesAndQuantitiesMap.isEmpty()){
            availableProductsWithSizesAndQtity.remove(this);
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
    public static List<ProductWithSizeAndQtity> getProductListByName(String name, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Results for phrase " + name + ":");
        String nameUpper= name.toUpperCase();
        List<ProductWithSizeAndQtity> availableProducts= parameterList
                .stream()
                .filter(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getName().toUpperCase().contains(nameUpper))
                .toList();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (availableProducts.isEmpty()){
            throw new UnavailableException("There are no available products with name " + name);
        }
        return availableProducts;
    }
    public static List<ProductWithSizeAndQtity> getProductListBySize(Size size, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Results for size "+ size + ":");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ProductWithSizeAndQtity> availableProducts= parameterList
                .stream()
                .filter(productWithSizeAndQtity -> productWithSizeAndQtity.getSizesAndQuantitiesMap().containsKey(size))
                .toList();
        if (availableProducts.isEmpty()){
            throw new UnavailableException("There are no available " + size + " sized products!");
        }
        return availableProducts;
    }
    public static List<ProductWithSizeAndQtity> getProductListByBrand(String brandName, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda, jedynie tego sleepa moze zmniejszyc lub usunac
        System.out.println("Searching for results for a given brand -> " + brandName + "...");
        String brandNameUpper= brandName.toUpperCase();
        List<ProductWithSizeAndQtity> availableProducts= parameterList
                .stream()
                .filter(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getBrand().toUpperCase().contains(brandNameUpper))
                .toList();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (availableProducts.isEmpty()){
            throw new UnavailableException("There are no available products for brand: " + brandName);
        }
        return availableProducts;

    }
    public static List<ProductWithSizeAndQtity> getProductListByCategoryAndBrand(Category category, String brandName, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Results for category " + category + " and brand " + brandName + ":" );
        List<ProductWithSizeAndQtity> selectedCategory= ProductWithSizeAndQtity.getProductListByCategory(category,parameterList);
        List<ProductWithSizeAndQtity> selectedBrand=ProductWithSizeAndQtity.getProductListByBrand(brandName,selectedCategory);

        return selectedBrand;
    }
    public static void printProductsFromSelectedList(List<ProductWithSizeAndQtity> resultList){
        // gotowa metoda
        if (resultList.isEmpty()){
            System.out.println("No results!");
            return;
        }
        for (ProductWithSizeAndQtity productWithSizeAndQtity : resultList){
            System.out.println(productWithSizeAndQtity.toString());
        }

    }
    public static List<ProductWithSizeAndQtity> getProductListByBrandAndName(String brandName, String name, List<ProductWithSizeAndQtity> parameterList) throws UnavailableException {
        // gotowa metoda
        System.out.println("Results for brand " + brandName + " and product name " + name + ":" );
        List<ProductWithSizeAndQtity> selectedBrand= ProductWithSizeAndQtity.getProductListByBrand(brandName,parameterList);
        List<ProductWithSizeAndQtity> selectedProductName= ProductWithSizeAndQtity.getProductListByName(name,selectedBrand);

        return selectedProductName;


    }

}
