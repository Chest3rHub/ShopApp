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

        System.out.println(LocalDate.now());
        SwingUtilities.invokeLater(() -> {
            ShopGUI.startLoginScreen();
        });
     //   Product.addProduct();
     //   Product.addProduct();
     //   Product.saveProductsToFile();
    }
}