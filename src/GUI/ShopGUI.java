package GUI;

import DTOs.PasswordRoleDTO;
import Models.Customers.Customer;
import Models.Employees.AbstractEmployee;
import Models.Employees.Role;
import Models.Order;
import Models.Products.Product;
import Models.Products.ProductWithSizeAndQtity;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShopGUI extends JFrame {

    public static final Font _FONT = new JLabel().getFont();
    public static final int _minPasswordLength=7;

    public static JFrame frame = new JFrame("Default");
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

                    if (role==Role.ADMIN){
                        adminLoggedIn();
                    } else if ( role==Role.MANAGER) {
                        managerLoggedIn();
                    } else if ( role==Role.CLIENT) {
                        long hash= login.hashCode();
                        String hashString= String.valueOf(hash);
                        Optional<Customer> customerLoggedIn= Customer.customers.stream().filter(customer -> customer.getLogin().equals(hashString)).findFirst();
                        if (!customerLoggedIn.isPresent()){
                            throw new Exception("No such customer");
                        }
                        clientLoggedIn(customerLoggedIn.get(), login);
                    }
//                    if (role==null){
//                        System.out.println("Bledny login lub haslo...");
//                    } else {
//                        System.out.println("Zalogowany jako "+ role);
//                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }

                // weryfikacja hashowanego hasla i loginu z baza danych itd...

                //  JOptionPane.showMessageDialog(frame, "Zalogowano jako: " + login);
            }
        });

        JButton backButton= logOutButton();
        backButton.setText("Back");

        secondPanel.add(loginLabel);
        secondPanel.add(loginField);
        secondPanel.add(passwordLabel);
        secondPanel.add(passwordField);
        secondPanel.add(backButton);
        secondPanel.add(loginButton);


        frame.setTitle("Shop App");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        frame.setVisible(true);
    }
    public static void loggedOutScreen(){
        BorderLayout borderLayout= new BorderLayout();
        JPanel mainPanel = new JPanel(borderLayout);
       // GridBagConstraints constraints= new GridBagConstraints();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setTitle("Shop App");
        frame.setLayout(new BorderLayout());


        mainPanel.setLayout(borderLayout);



        frame.add(mainPanel,BorderLayout.CENTER);


        JLabel welcomeLabel= new JLabel("Welcome to Shop App!",SwingConstants.CENTER);
        welcomeLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,19));
        // x kolumna, y wiersz
      //  constraints.fill=GridBagConstraints.HORIZONTAL;
      //  constraints.gridx=1;
      //  constraints.gridy=0;
     //   constraints.anchor= GridBagConstraints.PAGE_START;
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);


        // frame.setBackground(new Color(255,80,80));
      //   logo w rogu
        ImageIcon imageIcon= new ImageIcon("src/Graphics/shoppingCart.png");
        frame.setIconImage(imageIcon.getImage());

        // kolor tla
       // frame.getContentPane().setBackground(new Color(0,0,0));

        JPanel buttonPanel = new JPanel();

        JButton loginButton = new JButton("Sign in");
      //  constraints.gridx=0;
      //  constraints.gridy=1;
     //   constraints.gridwidth=2;
     //   constraints.anchor=GridBagConstraints.CENTER;
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");
      //  constraints.gridx=2;
     //   constraints.gridy=1;
     //   constraints.gridwidth=2;
    //    constraints.anchor=GridBagConstraints.CENTER;
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel,BorderLayout.CENTER);


       // mainPanel.add(new JLabel()); // Pusta etykieta jako wypełnienie




        JLabel creditsLabel= new JLabel("~ by Szymon Sawicki :)",SwingConstants.SOUTH_EAST);
      //  constraints.gridx=0;
     //   constraints.gridy=2;
     //   constraints.gridwidth=4;
     //   constraints.anchor=  GridBagConstraints.CENTER;
        mainPanel.add(creditsLabel,BorderLayout.SOUTH);

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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        int x = (screenWidth - windowWidth) / 2;
        int y = (screenHeight - windowHeight) / 2;

        // Ustaw położenie okna
        frame.setLocation(x, y);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainPanel);
        frame.setTitle("Shop App");
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        frame.setVisible(true);
    }


    public static void adminLoggedIn() {
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

            JButton logOutButton= logOutButton();

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
            buttonPanel.add(logOutButton);

            secondPanel.add(buttonPanel, BorderLayout.CENTER);
        // frame= new JFrame();
        frame.setTitle("Shop App");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    public static void managerLoggedIn(){

    }
    public static void clientLoggedIn(Customer customer, String enteredLogin){
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(450,350);
        
        JLabel welcomeLabel= new JLabel("Welcome " + enteredLogin + "!");
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
      //  secondPanel.add(welcomeLabel, BorderLayout.NORTH);

        JLabel creditsLabel= new JLabel("Credits: " + customer.getCredits());
       // secondPanel.add(creditsLabel,BorderLayout.NORTH);

        JPanel topPanel= new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));


        secondPanel.add(topPanel,BorderLayout.NORTH);

        JPanel downPanel= new JPanel(new FlowLayout());

      //  List<Order> orders= customer.getOrders();

        JButton goToCreditsButton= new JButton("Wallet");
        JButton logOutButton= logOutButton();
        JButton accountButton= new JButton("Account");
        JButton helpButton= new JButton("Help");
        JButton ordersButton= new JButton("Orders");
        JButton productsButton= new JButton("Products");
        JButton cartButton= new JButton("Cart");

        secondPanel.add(new JLabel());


        topPanel.add(creditsLabel);
        topPanel.add(new JLabel(""));
        topPanel.add(welcomeLabel);
        topPanel.add(cartButton);

        JPanel centerPanel= new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));



        centerPanel.add(goToCreditsButton);
        centerPanel.add(ordersButton);
        centerPanel.add(productsButton);

        downPanel.add(logOutButton);
        downPanel.add(accountButton);
        downPanel.add(helpButton);
        goToCreditsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCreditsScreen(customer,enteredLogin);
            }
        });
        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        ordersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        secondPanel.add(centerPanel,BorderLayout.CENTER);
        secondPanel.add(downPanel,BorderLayout.SOUTH);

        frame.setTitle("Menu: Client");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    public static void setClientData(Customer customer){
        // ustawic imie nazwisko adres dane kontaktowe itd
    }

    public static Role login(String loginEntered, String passwordEntered) throws Exception {


        String loginPasswordCombination= loginEntered+passwordEntered;
        long loginPasswordCombinationHash= loginPasswordCombination.hashCode();
        String combinedHashString= String.valueOf(loginPasswordCombinationHash);
        long hashedLoginLong= loginEntered.hashCode();
        String loginHashedString= String.valueOf(hashedLoginLong);
        PasswordRoleDTO loginToCheck= accounts.get(loginHashedString);
        if (loginToCheck==null){
            throw new Exception("Incorrect login or password") ;
        }
        String passwordToCheck= loginToCheck.getPassword();
//        long hashedPasswordLong= passwordToCheck.hashCode();
//        String passwordHashedString=String.valueOf(hashedPasswordLong);

        if (passwordToCheck.equals(combinedHashString)){
            return loginToCheck.getRole();
        }else {
            throw new Exception("Incorrect login or password");
        }
    }

    public static void readAccountsFromFile() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(accountsFileName))) {
            String line;
            line=br.readLine();
            line= br.readLine();
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
                    Customer customer= new Customer(loginHashed,passwordHashedString);
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
                    JOptionPane.showMessageDialog(frame, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }

                // rejestrowanie wpisanych danych
            }
        });
        JButton backButton= logOutButton();
        backButton.setText("Back");
        secondPanel.add(login);
        secondPanel.add(loginField);
        secondPanel.add(passwordLabel);
        secondPanel.add(passwordField);
        secondPanel.add(confirmPasswordLabel);
        secondPanel.add(confirmPasswordField);
        secondPanel.add(backButton);
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

        if (passwordEntered.toCharArray().length<_minPasswordLength){
            throw new Exception("Too short password! Minimum 7 characters!");
        }

        if (!passwordEntered.equals(confirmedPasswordEntered)){
            throw new Exception("Passwords are not the same!");
        }
        long passwordHashLong= (loginEntered+passwordEntered).hashCode();
        String passwordHash= String.valueOf(passwordHashLong);
        Customer customer= new Customer(loginHash,passwordHash);
        accounts.put(loginHash,new PasswordRoleDTO(passwordHash,Role.CLIENT));
        addCustomerToAccountsFile(customer);
        registeredAnAccountScreen();
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

        FileOutputStream fos = new FileOutputStream(accountsFileName, true);
        PrintStream ps = new PrintStream(fos);
        ps.println(customer.getLogin() + ";" + customer.getPassword() + ";" + _roleClientHash);

        ps.close();

//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFileName, true))) {
//                writer.write(customer.getLogin() + ";" + customer.getPassword() + ";" + _roleClientHash);
//                writer.newLine();
//            }
        }
        public static JButton logOutButton(){
            JButton logOutButton= new JButton("Log out");
            logOutButton.setBackground(new Color(255,94,90));
             logOutButton.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                     loggedOutScreen();
                 }
             });

            return logOutButton;
        }
        public static JButton backToMenuClient(Customer customer, String login){
            JButton backToMenuButton= new JButton("Back");
            backToMenuButton.setBackground(new Color(100,200,200));

            backToMenuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clientLoggedIn(customer,login);
                }
            });
            return backToMenuButton;
        }

        public static JButton addCreditsButton(){
         JButton button= new JButton();

         return button;
        }
        public static void returnToMenuAdmin(){

        }
        public static void returnToMenuManager(){

        }

        public static void viewAvailableProducts(){

        }
        public void addCreditsToAccount(Customer customer){

        }


        public static void addCreditsScreen(Customer customer, String loginEntered){
            secondPanel = new JPanel();
            secondPanel.setLayout(new BorderLayout());

            frame.setTitle("Add Credits");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);

            JLabel addCreditsLabel= new JLabel("Type the amount of credits to add:");
            addCreditsLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,14));
            addCreditsLabel.setHorizontalAlignment(JLabel.CENTER);
            addCreditsLabel.setVerticalAlignment(JLabel.TOP);


            JPanel buttonsPanel= new JPanel(new FlowLayout());

            JPanel textFieldPanel= new JPanel(new FlowLayout());



           // JLabel credits = new JLabel("Login:");
            JTextField creditsTextField = new JTextField();
            Dimension textFieldSize = new Dimension((int)(Toolkit.getDefaultToolkit().getScreenResolution() * 0.78),
                    (int)(Toolkit.getDefaultToolkit().getScreenResolution() * 0.39));
            creditsTextField.setPreferredSize(textFieldSize);

            textFieldPanel.setLayout(new BoxLayout(textFieldPanel,BoxLayout.PAGE_AXIS));
        //    creditsTextField.setBounds(new Rectangle(20,5));
            GridBagConstraints constraints= new GridBagConstraints();
            constraints.gridy=2;
            constraints.gridx=2;
            constraints.insets = new Insets(10, 10, 10, 10);

          //  textFieldPanel.setBounds(new Rectangle(20,5));

            textFieldPanel.add(creditsTextField);
            JButton addCreditsButton = new JButton("Pay");

            addCreditsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String credits= creditsTextField.getText();
                    try{
                        customer.addCredits(credits);
                        JOptionPane.showMessageDialog(frame, "Added "+ credits+ " credits successfully!");
                        creditsTextField.setText("");
                    }catch(Exception exception){
                        JOptionPane.showMessageDialog(frame, "Type an integer number!","Error",JOptionPane.ERROR_MESSAGE);
                    }

                }
            });
             JButton backToMenuClientButton= backToMenuClient(customer,loginEntered);

            buttonsPanel.add(backToMenuClientButton);
            buttonsPanel.add(addCreditsButton);

            secondPanel.add(addCreditsLabel,BorderLayout.NORTH);

            secondPanel.add(buttonsPanel,BorderLayout.SOUTH);
            secondPanel.add(new JPanel(),BorderLayout.WEST);
            secondPanel.add(new JPanel(),BorderLayout.EAST);
            secondPanel.add(textFieldPanel,BorderLayout.CENTER);

           // secondPanel.add(backToMenuClientButton);



            frame.setTitle("Wallet");
            frame.getContentPane().removeAll();
            frame.getContentPane().add(secondPanel);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            frame.setVisible(true);

        }
        public static void registeredAnAccountScreen(){
            secondPanel = new JPanel();
            secondPanel.setLayout(new BorderLayout());
            frame.setTitle("WELCOME!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            JLabel registeredLabel= new JLabel("Registered successfully!");

            registeredLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,24));
            //registeredLabel.setFont(new Font("Arial",Font.PLAIN,24));
            registeredLabel.setHorizontalAlignment(JLabel.CENTER);
            registeredLabel.setVerticalAlignment(JLabel.TOP);
            secondPanel.add(registeredLabel,BorderLayout.NORTH);

            JButton logInButton= new JButton("Continue");

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(logInButton);
            secondPanel.add(buttonPanel,BorderLayout.CENTER);
           // logInButton.setFont(new Font(_FONT.getFontName(),Font.PLAIN,24));
           // logInButton.setPreferredSize(new Dimension(2,2));
            logInButton.setBackground(new Color(160,255,125));

            logInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loggedOutScreen();
                }
            });
           // secondPanel.add(logInButton,BorderLayout.CENTER);


            //secondPanel.add(new JLabel()); // Pusta etykieta jako wypełnienie


            frame.setTitle("Shop App");
            frame.getContentPane().removeAll();
            frame.getContentPane().add(secondPanel);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            //frame.pack();
            frame.setVisible(true);
        }
}