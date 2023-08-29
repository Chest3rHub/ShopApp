package GUI;

import DTOs.PasswordRoleDTO;
import Models.Employees.AbstractEmployee;
import Models.Employees.Role;
import Models.Products.Product;
import Models.Products.ProductWithSizeAndQtity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ShopGUI extends JFrame {

    public static JFrame frame = new JFrame("Default");
    ;
    public static JPanel mainPanel;
    public static JPanel secondPanel;

    public final static String accountsFileName = "src/Data/Accounts.txt";
    // struktura pliku:
    // login;haslo;rola
    public final static String _roleAdminHash = String.valueOf(Role.ADMIN.toString().hashCode());
    public final static String _roleManagerHash = String.valueOf(Role.MANAGER.toString().hashCode());
    public final static String _roleClientHash = String.valueOf(Role.CLIENT.toString().hashCode());
    public final static String _roleConsultantHash = String.valueOf(Role.CONSULTANT.toString().hashCode());
    public final static String _roleWorkerHash = String.valueOf(Role.WORKER.toString().hashCode());

    public static HashMap<String, PasswordRoleDTO> accounts= new HashMap<>();

    public static void changeToLoginScreen() {

        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
       // JLabel welcomeLabel = new JLabel("Wprowadz login i hasło");
       // welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

       // secondPanel.add(welcomeLabel, BorderLayout.NORTH);

        frame.setTitle("Login to Shop App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        secondPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel loginLabel = new JLabel("Login:");
        JTextField loginField = new JTextField();

        JLabel passwordLabel = new JLabel("Hasło:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Zaloguj");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                try {
                    Role role = login(login,password);
                    if (role==null){
                        System.out.println("Bledny login lub haslo...");
                    } else {
                        System.out.println("Zalogowany jako "+ role);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                // weryfikacja hashowanego hasla i loginu z baza danych itd...

                //  JOptionPane.showMessageDialog(frame, "Zalogowano jako: " + login);
                changeScreenToLoggedIn();
            }
        });


        secondPanel.add(loginLabel);
        secondPanel.add(loginField);
        secondPanel.add(passwordLabel);
        secondPanel.add(passwordField);
        secondPanel.add(new JLabel()); // Pusta etykieta jako wypełnienie
        secondPanel.add(loginButton);


        frame.setTitle("Shop App");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        frame.setVisible(true);
    }
    public static void startingScreen(){
        JPanel mainPanel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Welcome to Shop App!");

        // logo w rogu
      //  ImageIcon imageIcon= new ImageIcon("plik.png");
      //  frame.setIconImage(imageIcon.getImage());

        // kolor tla
       // frame.getContentPane().setBackground(new Color(0,0,0));
        frame.setSize(300, 200);
        JButton loginButton = new JButton("Zaloguj");
        JButton registerButton = new JButton("Utworz nowe konto");
        mainPanel.add(new JLabel()); // Pusta etykieta jako wypełnienie
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeToLoginScreen();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // register
            }
        });


        frame.getContentPane().add(mainPanel);

        frame.setVisible(true);
    }


    public static void changeScreenToLoggedIn() {
            secondPanel = new JPanel();
            secondPanel.setLayout(new BorderLayout());

            JLabel welcomeLabel = new JLabel("Witaj");
            welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            secondPanel.add(welcomeLabel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout());


            JButton button1 = new JButton("Wyswietl produkty");
            JButton button2 = new JButton("Wyswietl produkty");
            JButton button3 = new JButton("Dodaj produkt");

            button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AbstractEmployee.showEmployees();
                }
            });

            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Product.showProducts();
                }
            });
            button3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ProductWithSizeAndQtity.showAllAvailableProductsWithSizesAndQtity();
                }
            });
            buttonPanel.add(button1);
            buttonPanel.add(button2);
            buttonPanel.add(button3);

            secondPanel.add(buttonPanel, BorderLayout.CENTER);
        // frame= new JFrame();
        frame.setTitle("Shop App");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public static Role login(String loginEntered, String passwordEntered) throws Exception {


        String loginPasswordCombination= loginEntered+passwordEntered;
        long loginPasswordCombinationHash= loginPasswordCombination.hashCode();
        String combinedHashString= String.valueOf(loginPasswordCombinationHash);
        long hashedLoginLong= loginEntered.hashCode();
        String loginHashedString= String.valueOf(hashedLoginLong);
        PasswordRoleDTO loginToCheck= accounts.get(loginHashedString);
        if (loginToCheck==null){
            throw new Exception("Podano zly login lub haslo") ;
        }
        String passwordToCheck= loginToCheck.getPassword();
        long hashedPasswordLong= passwordToCheck.hashCode();
        String passwordHashedString=String.valueOf(hashedPasswordLong);

        if (passwordToCheck.equals(combinedHashString)){
            return loginToCheck.getRole();
        }else {
            throw new Exception("Podano zly login lub haslo");
        }
    }

    public static void readAccountsFromFile() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(accountsFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                String loginHashed = values[0].trim();
                String passwordHashedString = values[1].trim();
                String roleHashedString = values[2].trim();
                Role role = null;
                if (roleHashedString.equals(_roleAdminHash)){
                    role= Role.ADMIN;
                } else if (roleHashedString.equals(_roleManagerHash)) {
                    role=Role.MANAGER;
                } else if (roleHashedString.equals(_roleClientHash)) {
                    role=Role.CLIENT;
                } else if (roleHashedString.equals(_roleWorkerHash)) {
                    role=Role.WORKER;
                } else if (roleHashedString.equals(_roleConsultantHash)) {
                    role=Role.CONSULTANT;
                }
               // int hashedPassword = Integer.parseInt(passwordHashedString);
                accounts.put(loginHashed,new PasswordRoleDTO(passwordHashedString,role));

            }
        }
    }
    public static void Register(){

    }
    public static void showAllAccounts(){
        for (Map.Entry<String, PasswordRoleDTO> entry : accounts.entrySet()) {
            String key = entry.getKey();
            PasswordRoleDTO passwordRoleDTO = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + passwordRoleDTO.getPassword() + ";" + passwordRoleDTO.getRole());
        }
    }
}