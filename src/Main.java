import GUI.ShopGUI;
import Models.Customers.Customer;
import Models.Employees.AbstractEmployee;
import Models.Order;
import Models.Products.Product;
import Models.Shop;

import javax.swing.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        // konto ADMIN:
        // login: admin
        // password: Lebioda

        // konto MANAGER:
        // login: manager
        // password: Szef


        // jedno konto dla admina, jedno dla managera, wiele dla klientow

        // produkty, pozniej seeduje ilosci i rozmiary a na kooniec pracownikow
        Shop.openShop();
        Product.showProducts();
        System.out.println("======");
        AbstractEmployee.showEmployees();
        System.out.println("======");
        try {
          //  Shop.seedProductData();

            // pusta metoda
            Shop.seedEmployeeData();
        }catch (Exception e){
            e.printStackTrace();
           // e.getMessage();
        }

        // zeby sie zalogowac porownuje hash kombinacji hasla i loginu, poniewaz login jest unikalny dla kazdego
        // wiec mozna to potraktowac jak salt do zabezpieczenia
        // czyli osoby o roznych loginach ale tym samym hasle beda mialy rozne hashe jako "password hash"

//        System.out.println(LocalDate.now());
//        String admin="manager";
//        System.out.println(admin.hashCode());
//        String haslo="Szef";
//        int hashed= (admin+haslo).hashCode();
//
//        System.out.println(hashed);
//        System.out.println(ShopGUI._roleManagerHash);
//        SwingUtilities.invokeLater(() -> {
//            try {
//                ShopGUI.readAccountsFromFile();
//                ShopGUI.showAllAccounts();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            ShopGUI.loggedOutScreen();
//        });
        try {
            ShopGUI.readAccountsFromFile();
            ShopGUI.readCustomersFromFile();
            Order.readOrdersFromFile();
            Customer.addOrdersToCustomersStartUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  ShopGUI.showAllAccounts();
        ShopGUI shopGUI= new ShopGUI();

        //   Product.addProduct();
     //   Product.addProduct();
     //   Product.saveProductsToFile();
    }
}