package Models.Products;


import Models.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Product {

    public static int idCounter=1;
    int id;
    Category category;
    String name;
    String brand;
    double price;
    String description;
    boolean availability;
    public static List<Product> allProducts= new ArrayList<>();

    public Product(Category category, String name, String brand, double price, String description){
        this.id= idCounter++;
        this.category=category;
        this.name=name;
        this.brand=brand;
        this.price=price;
        this.description=description;

    }

    public static void addProduct(){
        try {
            Scanner scanner= new Scanner(System.in);
            scanner.useDelimiter("\n");
            System.out.println("Podaj kategorie: ");

            String categoryValue= scanner.next();
            String categoryUpper= categoryValue.toUpperCase();
            Category category= Category.valueOf(categoryUpper);
            System.out.println("Podaj nazwe produktu: ");
            String name= scanner.next();
            System.out.println("Podaj marke produktu: ");
            String brand= scanner.next();
            System.out.println("Podaj cene produktu: ");
            double price= scanner.nextDouble();
            System.out.println("Podaj opis: ");
            String description= scanner.next();
            //  System.out.println("Podaj ilosc");
            // int quantity= scanner.nextInt();

            allProducts.add(new Product(category,name,brand,price,description));
            System.out.println("DODANO PRODUKT DO ASORTYMENTU!");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Podano zla wartosc!");
        }


    }

    public String toString(){
        return "ID: "+ id
                + ", CATEGORY: " + category
                + ", NAZWA: " + name
                + ", MARKA: " + brand
                + ", CENA: " + price + "PLN"
                + ", OPIS: " + description
                + ", DOSTEPNOSC: " + availability;
    }

    public static void showProducts(){
        for (Product product : allProducts){
            System.out.println(product.toString());
        }
    }


}