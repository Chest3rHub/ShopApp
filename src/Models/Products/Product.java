package Models.Products;


import Models.Category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Product {

    public static final String productsFileName="src/Data/Products.txt";
    public static int idCounter=1;
    int id;
    Category category;
    String name;
    String brand;
    double price;
    String description;
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
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedPrice = decimalFormat.format(price);
        return "ID: "+ id
                + ", CATEGORY: " + category
                + ", NAZWA: " + name
                + ", MARKA: " + brand
                + ", CENA: " + formattedPrice + "PLN"
                + ", OPIS: " + description;
    }

    public static void showProducts(){
        for (Product product : allProducts){
            System.out.println(product.toString());
        }
    }


    public static void readProductsFromFile (){
        try{
            int productsCounter=0;
            BufferedReader fileReader= new BufferedReader(new FileReader(productsFileName));
            Scanner scanner= new Scanner(fileReader);


            while(scanner.hasNextLine()){


                String line= scanner.nextLine();
                Scanner lineScanner= new Scanner(line);
                lineScanner.useDelimiter(";");
                    int id= lineScanner.nextInt();
                    String categoryString= lineScanner.next();
                    Category category= Category.valueOf(categoryString);
                    String name= lineScanner.next();
                    String brand= lineScanner.next();
                    String priceString=  lineScanner.next();
                    double price= Double.parseDouble(priceString);
                    String description= lineScanner.next();

                //System.out.println(" CENA " + priceString);
                //System.out.println(price);
                allProducts.add(new Product(category,name,brand,price,description));
            }
            idCounter+=productsCounter;
            // nowo dodawane produkty beda mialy id ktorego jeszcze nie ma w pliku
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveProductsToFile(){
        try{
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(productsFileName));
            for (Product product : allProducts){
                String data=product.id + ";" + product.category+ ";"
                        +product.name + ";"  + product.brand + ";"
                        + product.price + ";" + product.description + "\n";

                fileWriter.write(data);
                fileWriter.flush();
            }
            fileWriter.close();
          //  System.out.println("Zapisano do pliku!");
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}