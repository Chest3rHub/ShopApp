package Models;

import DTOs.ProductInCartDTO;
import Models.Customers.Customer;
import Models.Products.Product;
import Models.Products.Size;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Order {
    public static List<Order> allOrders= new ArrayList<>();
    public final static String ordersFileName= "src/Data/Orders.txt";
    public static double totalRevenue=0;
    public static int idCounter=10531;
    int idOrder;
    //String loginHash;
   // List<Product> products= new ArrayList<>();
    // raczej lista product with size and qtity

    // chyba zmienic na ProductDTO
    // moze dodac date zlozenia zamowienia i statyczna liste wszystkich zamowien
    LocalDate orderedAt;
    List<ProductInCartDTO> orderedProducts;
    double totalCost;

    public Order(List<ProductInCartDTO> orderedProducts) {
        this.idOrder = idCounter;
       // this.loginHash = loginHash;
        this.orderedAt = LocalDate.now();
        this.orderedProducts = orderedProducts;
        this.totalCost = this.calculateCost();
        totalRevenue+=this.totalCost;
        allOrders.add(this);
        idCounter++;
    }

    public Order(int idOrder, LocalDate date ,List<ProductInCartDTO> orderedProducts) {
        this.idOrder = idOrder;
        this.orderedAt=date;
        this.orderedProducts = orderedProducts;
        this.totalCost = this.calculateCost();
        totalRevenue+=this.totalCost;
        allOrders.add(this);
        idCounter++;
    }

    /**
     * This method saves orders to file
     */
    public static void saveOrdersToFile(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(ordersFileName));
            bufferedWriter.write("idOrder;orderDate;totalCost;orderedProducts\n");
            for (Order order : allOrders) {
                bufferedWriter.write(order.idOrder+"!"
                        + order.orderedAt +"!"
                        + order.totalCost + "!"
                        + order.orderedProducts);
                bufferedWriter.write("\n");
            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method gets selected order's info
     * @return String value of the information
     */
    public String getSelectedOrderInfo(){
        String info="";
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        for (ProductInCartDTO product : this.orderedProducts){
            String formattedPrice = decimalFormat.format(product.getProductCost() * product.getQuantity());
            String noCommaPrice= formattedPrice.replace(",",".");
            double finalCost= Double.parseDouble(noCommaPrice);
            info+="BRAND: " + product.getProductBrand()
                    + ", PRODUCT: " + product.getProductName()
                    + ", SIZE: " + product.getSize()
                    + ", QUANTITY: " + product.getQuantity()
                    + ", COST: " + finalCost + "PLN"
                    + "\n";
        }

        return info;
    }

    /**
     * This method reads orders from file
     * @throws Exception when file with provided path not found
     */
    public static void readOrdersFromFile() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(ordersFileName))) {
            String line;
            line=br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("!");
                String idOrderString = values[0];
                int idOrder = Integer.parseInt(idOrderString);
                String dateString= values[1];
                LocalDate date= LocalDate.parse(dateString);
                String costString= values[2];
                double cost= Double.parseDouble(costString);
                String products=values[3];

                List<ProductInCartDTO> productInCartDTOList= new ArrayList<>();
                String toScan= products.replace("[","");
                String toScanFinally= toScan.replace("]","");

                if (!toScanFinally.equals("")){
                    Scanner scanner= new Scanner(toScanFinally);
                    scanner.useDelimiter(",");
                    //   String oneProduct= scanner.next();
                    while(scanner.hasNext()){
                        String oneProduct= scanner.next();
//                        System.out.println("Cala linia do zeskanowania pod spodem:");
//                        System.out.println(oneProduct);
                        Scanner oneProductScanner= new Scanner(oneProduct);
                        oneProductScanner.useDelimiter(";");
                        String idString= oneProductScanner.next();
                        String idNoWhite =idString.trim();
                        int idProduct=Integer.parseInt(idNoWhite);
                        // System.out.println("Pobrane id " + idProduct);
                        String category=oneProductScanner.next();
                        // System.out.println("Pobrana kategoria "+ category);

                        String nazwa= oneProductScanner.next();
                        // System.out.println("Pobrana nazwa " + nazwa);
                        String marka= oneProductScanner.next();

                        //  System.out.println("Pobrana marka: "+ marka);
                        String koszt= oneProductScanner.next();
                        // System.out.println("Pobrany koszt+ " + koszt);

                        String opis= oneProductScanner.next();
                        // System.out.println("Pobrany opis: " + opis);

                        Size size= Size.valueOf(oneProductScanner.next());
                        // System.out.println("Pobrany rozmiar : " + size);
                        int quantity= oneProductScanner.nextInt();
                        // System.out.println("Pobrana ilosc "+ quantity);
                        // juz git tylko ogarnac ID bo sie przesuwa

                        productInCartDTOList.add(new ProductInCartDTO(idProduct-1,size,quantity));
                    }
                    Order o1= new Order(idOrder,date,productInCartDTOList);

                }
            }
        }

    }


    /**
     * This method calculates cost of the current cart
     * @return Double value of this calculated cost
     */

    public double calculateCost(){
        double cost=0;
        for (ProductInCartDTO product : orderedProducts){
            int idProduct=product.getIdProduct();
            double itemCost = Product.allProducts.get(idProduct).getPrice();
            cost+=product.getQuantity() * itemCost;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedPrice = decimalFormat.format(cost);
        String noCommaPrice= formattedPrice.replace(",",".");
        double finalCost= Double.parseDouble(noCommaPrice);
        return finalCost;
    }

    public static List<Order> getAllOrders() {
        return allOrders;
    }

    public static void setAllOrders(List<Order> allOrders) {
        Order.allOrders = allOrders;
    }

    public static double getTotalRevenue() {
        return totalRevenue;
    }

    public static void setTotalRevenue(double totalRevenue) {
        Order.totalRevenue = totalRevenue;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Order.idCounter = idCounter;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public LocalDate getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDate orderedAt) {
        this.orderedAt = orderedAt;
    }

    public List<ProductInCartDTO> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<ProductInCartDTO> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String toString(){
        return this.idOrder + ";"
                + this.orderedAt + ";"
                + this.totalCost + ";"
                + this.orderedProducts;


        // moze zmienic sredniki na cos innego bo jakbym chcial skanowac po sredniku to przedmioty w klasie
        // productwithsizeandqtity sa odzielone chyba srednikami
    }
}