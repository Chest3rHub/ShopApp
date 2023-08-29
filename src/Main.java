import GUI.ShopGUI;
import Models.Employees.AbstractEmployee;
import Models.Products.Product;
import Models.Shop;

import javax.swing.*;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Shop.openShop();
        Product.showProducts();
        System.out.println("======");
        AbstractEmployee.showEmployees();
        System.out.println("======");
        try {
          //  Shop.seedProductData();
            Shop.seedEmployeeData();
        }catch (Exception e){
            e.printStackTrace();
           // e.getMessage();
        }

        // zeby sie zalogowac porownuje hash kombinacji hasla i loginu, poniewaz login jest unikalny dla kazdego
        // wiec mozna to potraktowac jak salt do zabezpieczenia
        // czyli osoby o roznych loginach ale tym samym hasle beda mialy rozne hashe jako "password hash"

        System.out.println(LocalDate.now());
        String admin="admin";
        System.out.println(admin.hashCode());
        String haslo="Lebioda";
        int hashed= (admin+haslo).hashCode();
        String haslo2="Passwordto123!";
        System.out.println(hashed);
        System.out.println(haslo2.hashCode());
        SwingUtilities.invokeLater(() -> {
            try {
                ShopGUI.readAccountsFromFile();
                ShopGUI.showAllAccounts();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ShopGUI.startingScreen();
        });
     //   Product.addProduct();
     //   Product.addProduct();
     //   Product.saveProductsToFile();
    }
}