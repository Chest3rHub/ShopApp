package Models.Products;


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
        allProducts.add(this);
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

    public String getProductInfo(){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedPrice = decimalFormat.format(price);
        return "ID: "+ id
                + ", CATEGORY: " + category
                + ", NAZWA: " + name
                + ", MARKA: " + brand
                + ", CENA: " + formattedPrice + "PLN"
                + ", OPIS: " + description;
    }
public String toString(){
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    String formattedPrice = decimalFormat.format(price);
    String noCommaPrice= formattedPrice.replace(",",".");
    return id
            + ";" + category
            + ";" + name
            + ";" + brand
            + ";" + noCommaPrice
            + ";" + description;
}

    public static void showProducts(){
        for (Product product : allProducts){
            System.out.println(product.toString());
        }
    }


    public static void readProductsFromFile (){
        /**
         * This method reads all products from file in the beginning of the program
         */
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
                    Product product= new Product(category,name,brand,price,description);
            }
            idCounter+=productsCounter;

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static void saveProductsToFile(){
        /**
         * This method saves products to file
         */
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
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}