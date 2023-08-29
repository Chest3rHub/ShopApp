package GUI;

import DTOs.PasswordRoleDTO;
import Models.Customers.Customer;
import Models.Employees.AbstractEmployee;
import Models.Employees.Role;
import Models.Products.Product;
import Models.Products.ProductWithSizeAndQtity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Sign in");
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

      //   logo w rogu
        ImageIcon imageIcon= new ImageIcon("src/Graphics/shoppingCart.png");
        frame.setIconImage(imageIcon.getImage());

        // kolor tla
       // frame.getContentPane().setBackground(new Color(0,0,0));
        frame.setSize(300, 200);
        JButton loginButton = new JButton("Zaloguj");
        JButton registerButton = new JButton("Utworz nowe konto");
       // mainPanel.add(new JLabel()); // Pusta etykieta jako wypełnienie
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
                changeToRegisterScreen();
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
            line=br.readLine();
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
                    Customer.customers.add(new Customer(loginHashed,passwordHashedString));
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
    public static void changeToRegisterScreen(){


        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setTitle("Login to Shop App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        secondPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel login = new JLabel("Login:");
        JTextField loginField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        JPasswordField confirmPasswordField = new JPasswordField();



        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();

                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                char[] confirmedPasswordChars = confirmPasswordField.getPassword();
                String confirmedPassword = new String(confirmedPasswordChars);

                try {
                    register(login,password,confirmedPassword);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                // rejestrowanie wpisanych danych
            }
        });
        secondPanel.add(login);
        secondPanel.add(loginField);
        secondPanel.add(passwordLabel);
        secondPanel.add(passwordField);
        secondPanel.add(confirmPasswordLabel);
        secondPanel.add(confirmPasswordField);
        secondPanel.add(new JLabel()); // Pusta etykieta jako wypełnienie
        secondPanel.add(registerButton);


        frame.setTitle("Shop App");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        frame.setVisible(true);

    }
    public static void register(String loginEntered, String passwordEntered, String confirmedPasswordEntered) throws Exception{
        int loginHashLong= loginEntered.hashCode();
        String loginHash= String.valueOf(loginHashLong);
        if (accounts.containsKey(loginHash)){
            throw new Exception("This login is already taken!");
        }

        if (!passwordEntered.equals(confirmedPasswordEntered)){
            throw new Exception("Passwords are not the same!");
        }
        long passwordHashLong= (loginEntered+passwordEntered).hashCode();
        String passwordHash= String.valueOf(passwordHashLong);
        Customer customer= new Customer(loginHash,passwordHash);
        addCustomerToAccountsFile(customer);
// dodawanie konta do bazy danych itd
    }
    public static void showAllAccounts(){
        for (Map.Entry<String, PasswordRoleDTO> entry : accounts.entrySet()) {
            String key = entry.getKey();
            PasswordRoleDTO passwordRoleDTO = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + passwordRoleDTO.getPassword() + ";" + passwordRoleDTO.getRole());
        }
    }
    public static void addCustomerToAccountsFile(Customer customer) throws Exception{

        FileOutputStream fos = new FileOutputStream(accountsFileName, true); // true oznacza tryb dopisywania
        PrintStream ps = new PrintStream(fos);
        ps.println(customer.getLogin() + ";" + customer.getPassword() + ";" + _roleClientHash);

        ps.close();

//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFileName, true))) {
//                writer.write(customer.getLogin() + ";" + customer.getPassword() + ";" + _roleClientHash);
//                writer.newLine(); // Opcjonalnie dodaj nową linię po dopisaniu zawartości
//            }
        }
}