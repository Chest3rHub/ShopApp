import GUI.ShopGUI;
import Models.Customers.Customer;
import Models.Employees.AbstractEmployee;
import Models.Employees.Consultant;
import Models.Order;
import Models.Products.Product;
import Models.Shop;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ConcurrentModificationException;

public class Main {

    public static void main(String[] args) {
        // konto ADMIN:
        // login: admin
        // password: Lebioda

        // konto MANAGER:
        // login: manager
        // password: Szef


        // test
        System.out.println("Lebioda".hashCode());
        System.out.println((("Szymon"+ "Lebioda").hashCode()));
        long test=("Szymon"+ "Lebioda").hashCode();
        String sss=String.valueOf(test);
        System.out.println(sss);
        System.out.println(sss.hashCode());

        System.out.println(("-1802365190"+"Lebioda").hashCode());


        // jedno konto dla admina, jedno dla managera, wiele dla klientow

        try{
            Shop.openShop();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
          //  Shop.seedProductData();

            // pusta metoda
            Shop.seedEmployeeData();

            ShopGUI.readAccountsFromFile();
            ShopGUI.readCustomersFromFile();
            Order.readOrdersFromFile();
            Customer.addOrdersToCustomersStartUp();
        }catch (Exception e){
            e.printStackTrace();
        }

        ShopGUI shopGUI= new ShopGUI();

    }
}