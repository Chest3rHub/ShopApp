package GUI;

import DTOs.PasswordRoleDTO;
import DTOs.ProductInCartDTO;
import Exceptions.NotEnoughProductsException;
import Exceptions.UnavailableException;
import Models.Customers.Customer;
import Models.Employees.AbstractEmployee;
import Models.Employees.Role;
import Models.Order;
import Models.Products.Category;
import Models.Products.Product;
import Models.Products.ProductWithSizeAndQtity;
import Models.Products.Size;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopGUI extends JFrame {

    public static final Font _FONT = new JLabel().getFont();
    public static final int _minPasswordLength=7;

    public static JFrame frame = new JFrame("Default");
    public static JPanel mainPanel;
    public static JPanel secondPanel;

    public final static String _RED="\u001B[31m";

    public final static String _RESET="\u001B[0m";
    public final static String _GREEN="\u001B[32m";
    public final static String _BLUE="\u001B[34m";
    public final static String _YELLOW="\u001B[33m";

    public final static String _PURPLE="\u001B[35m";

    public final static String _CYAN="\u001B[36m";
    public final static String customersFileName="src/Data/Customers.txt";
    public final static String accountsFileName = "src/Data/Accounts.txt";
    // struktura pliku:
    // login;haslo;rola
    public final static String _roleAdminHash = String.valueOf(Role.ADMIN.toString().hashCode());
    public final static String _roleManagerHash = String.valueOf(Role.MANAGER.toString().hashCode());
    public final static String _roleClientHash = String.valueOf(Role.CLIENT.toString().hashCode());
    public final static String _roleConsultantHash = String.valueOf(Role.CONSULTANT.toString().hashCode());
    public final static String _roleWorkerHash = String.valueOf(Role.WORKER.toString().hashCode());

    public static HashMap<String, PasswordRoleDTO> accounts= new HashMap<>();

    public ShopGUI(){
        // dodac jeszcze aktualizacje klientow, produktow itd
        loggedOutScreen();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    savePasswordChangesToFile();
                    saveCustomersChangesToFile();
                    Order.saveOrdersToFile();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

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
                        Customer customerLoggedIn= Customer.customers.get(hashString);
                        if (customerLoggedIn==null){
                            throw new Exception("No such customer");
                        }
                        clientLoggedIn(customerLoggedIn, login);
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
                accountScreenCustomer(customer,enteredLogin);
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
                ordersMenuClient(customer,enteredLogin);
            }
        });
        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productsMenuClient(customer,enteredLogin);
            }
        });
        cartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cartScreen(customer, enteredLogin);
            }
        });

        secondPanel.add(centerPanel,BorderLayout.CENTER);
        secondPanel.add(downPanel,BorderLayout.SOUTH);

        frame.setTitle("Menu: Client");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.pack();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    public static void productsMenuClient(Customer customer, String loginEntered){

        frame.setSize(575,400);
        secondPanel = new JPanel(new BorderLayout());

        secondPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        DefaultListModel<ProductWithSizeAndQtity> productModel = new DefaultListModel<>();
        JList<ProductWithSizeAndQtity> productList = new JList<>(productModel);

        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
            productModel.addElement(product);
        }
        JPanel comboBoxPanel= new JPanel(new GridLayout(8,1));

        JComboBox<String> sizeComboBox = new JComboBox<>();
        sizeComboBox.setSize(100,100);
        sizeComboBox.setEnabled(false);

        JComboBox<String> quantityComboBox = new JComboBox<>();
        quantityComboBox.setSize(100,100);
        quantityComboBox.setEnabled(false);

        JLabel sizeLabel= new JLabel("SIZE");
        sizeLabel.setVerticalAlignment(JLabel.CENTER);

        comboBoxPanel.add(sizeLabel);


        comboBoxPanel.add(sizeComboBox);

        JLabel amountLabel= new JLabel("AVAILABLE AMOUNT:");
        amountLabel.setVerticalAlignment(JLabel.CENTER);


        comboBoxPanel.add(amountLabel);


        comboBoxPanel.add(quantityComboBox);

        JLabel costLabel= new JLabel();
        comboBoxPanel.add(costLabel);
        JLabel costLabelPLN= new JLabel();
        comboBoxPanel.add(costLabelPLN);

        sizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object sizeObject= sizeComboBox.getSelectedItem();
                String sizeString= String.valueOf(sizeObject);
                Size size= Size.valueOf(sizeString);
                ProductWithSizeAndQtity selectedProduct = productList.getSelectedValue();
                int quantity= selectedProduct.getSizesAndQuantitiesMap().get(size);
                String[] parts= new String[quantity];
                for (int i=0; i <quantity; i++){
                    parts[i]=String.valueOf(i+1);
                }
                quantityComboBox.setModel(new DefaultComboBoxModel<>(parts));
                quantityComboBox.setEnabled(true);
            }
        });

        quantityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object quantityObject= quantityComboBox.getSelectedItem();
                String quantityString= String.valueOf(quantityObject);
                int quantity= Integer.parseInt(quantityString);

                costLabel.setText("Cost: ");
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String formattedPrice = decimalFormat.format(productList.getSelectedValue().getProduct().getPrice()*quantity);
                String noCommaPrice= formattedPrice.replace(",",".");
                double formattedCost=Double.parseDouble(noCommaPrice);
                costLabelPLN.setText(formattedCost+ "PLN");
            }
        });
        productList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof ProductWithSizeAndQtity) {
                    value = "BRAND: " +((ProductWithSizeAndQtity) value).getProduct().getBrand()
                            + ", PRODUCT: " + ((ProductWithSizeAndQtity) value).getProduct().getName()
                            + ", PRICE: " +((ProductWithSizeAndQtity) value).getProduct().getPrice()
                            + ", SIZES: " +((ProductWithSizeAndQtity) value).getSizesAndQuantitiesMap();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });



        productList.addListSelectionListener(e -> {
            // Po wybraniu produktu, aktualizujemy dostępne rozmiary w JComboBox
            costLabel.setText("");
            costLabelPLN.setText("");
            ProductWithSizeAndQtity selectedProduct = productList.getSelectedValue();
            if (selectedProduct != null) {
                String [] partsEmpty= new String[0];
                quantityComboBox.setModel(new DefaultComboBoxModel<>(partsEmpty));
                List<String> sizesToArrayList= new ArrayList<>();
                // Rozdzielamy napis, aby uzyskać dostępne rozmiary
                LinkedHashMap<Size,Integer> sizes= selectedProduct.getSizesAndQuantitiesMap();
                // mozna usunac ta liste pod spodem
                List<Integer> quantitiesToArrayList= new ArrayList<>();
                if (sizes.containsKey(Size.XS)){
                    sizesToArrayList.add("XS");
                    quantitiesToArrayList.add(sizes.get(Size.XS));
                }
                if (sizes.containsKey(Size.S)){
                    sizesToArrayList.add("S");
                    quantitiesToArrayList.add(sizes.get(Size.S));
                }
                if (sizes.containsKey(Size.M)){
                    sizesToArrayList.add("M");
                    quantitiesToArrayList.add(sizes.get(Size.M));
                }
                if (sizes.containsKey(Size.L)){
                    sizesToArrayList.add("L");
                    quantitiesToArrayList.add(sizes.get(Size.L));
                }
                if (sizes.containsKey(Size.XL)){
                    sizesToArrayList.add("XL");
                    quantitiesToArrayList.add(sizes.get(Size.L));
                }
                if (sizes.containsKey(Size.ONESIZE)){
                    sizesToArrayList.add("ONESIZE");
                    quantitiesToArrayList.add(sizes.get(Size.ONESIZE));
                }
                String[] parts= sizesToArrayList.toArray(new String[0]);


                    sizeComboBox.setModel(new DefaultComboBoxModel<>(parts));
                    if (sizes.isEmpty()){
                        sizeComboBox.setEnabled(false);
                        quantityComboBox.setModel(new DefaultComboBoxModel<>(partsEmpty));
                        quantityComboBox.setEnabled(false);
                    } else {
                        sizeComboBox.setEnabled(true);
                    }



            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backButton = backToMenuClient(customer,loginEntered);
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    try {
                        if (productList.getSelectedValue()==null){
                            throw new Exception("Choose a product!");
                        }
                        if (sizeComboBox.getSelectedItem() == null){
                            throw new Exception("Choose a size!");
                        }
                        if (quantityComboBox.getSelectedItem()==null){
                            throw new Exception("Choose a quantity!");
                        }
                        // id minus jeden
                        int id= productList.getSelectedValue().getProduct().getId()-1;
                        System.out.println("Dodawane do koszyka id produktu id-1 :" + id);
                        Object sizeObject= sizeComboBox.getSelectedItem();
                        String sizeString = String.valueOf(sizeObject);
                        Size size= Size.valueOf(sizeString);

                        Object quantityObject= quantityComboBox.getSelectedItem();
                        String quantityString= String.valueOf(quantityObject);
                        int quantity= Integer.parseInt(quantityString);

                        customer.addToCart(new ProductInCartDTO(id,size,quantity));
                        JOptionPane.showMessageDialog(frame,"Added to cart!", "Congrats :)", JOptionPane.PLAIN_MESSAGE);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                    }


            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(addToCartButton);


        secondPanel.add(new JScrollPane(productList), BorderLayout.WEST);
        secondPanel.add(comboBoxPanel, BorderLayout.CENTER);
        secondPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.pack();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public static void ordersMenuClient(Customer customer, String loginEntered) {
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(600, 500);

        JLabel ordersHistoryLabel = new JLabel("History: ");
        ordersHistoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secondPanel.add(ordersHistoryLabel, BorderLayout.NORTH);

        DefaultListModel<Order> orderModel = new DefaultListModel<>();
        JList<Order> ordersList = new JList<>(orderModel);
        for (Order order : customer.getOrders()) {
            orderModel.addElement(order);
        }

        ordersList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Order) {
                    value = "ID: " + ((Order) value).getIdOrder()
                            + ", DATE: " + ((Order) value).getOrderedAt()
                            + ", COST: " + ((Order) value).getTotalCost() + "PLN";
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        JTextArea productInfoTextArea = new JTextArea();
        productInfoTextArea.setEditable(false);
        productInfoTextArea.setWrapStyleWord(true);
        productInfoTextArea.setLineWrap(true);

       // JLabel orderProductsInfoLabel = new JLabel();

        ordersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Order order = ordersList.getSelectedValue();
              //  orderProductsInfoLabel.setText("ORDERED: \n" + order.getSelectedOrderInfo());
                productInfoTextArea.setText("ORDERED: \n" + order.getSelectedOrderInfo());
            }
        });

        // Tworzymy panel, który będzie zawierać ordersList i orderProductsInfoLabel
        JPanel listAndInfoPanel = new JPanel(new BorderLayout());
        listAndInfoPanel.add(new JScrollPane(ordersList), BorderLayout.CENTER);
     //   listAndInfoPanel.add(orderProductsInfoLabel, BorderLayout.SOUTH);

        listAndInfoPanel.add(productInfoTextArea, BorderLayout.SOUTH);
        JButton backButton = backToMenuClient(customer, loginEntered);

        // Tworzymy panel na przycisk backButton
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);

        // Dodajemy panele do secondPanel
        secondPanel.add(listAndInfoPanel, BorderLayout.CENTER);
        secondPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setTitle("Orders");
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
    public static void readCustomersFromFile() throws Exception{

        ////dodac jeszcze czytanie historii zamowien
        try (BufferedReader br = new BufferedReader(new FileReader(customersFileName))) {
            String line;
            line=br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("!");
                String loginHashed = values[0];
                String firstName= values[1];
                String lastName= values[2];
                String address= values[3];
                String tel= values[4];
                int telNumber= Integer.parseInt(tel);
                String credits=values[5];
                double creditsDouble= Double.parseDouble(credits);
                String email=values[6];
                String currentCart=values[7];
                String orders=values[8];

                System.out.println("Pobrany koszyk : " + currentCart);
                System.out.println("Pobrane id zamowien: " + orders);

                List<ProductInCartDTO> cartList= new ArrayList<>();

                String toScan= currentCart.replace("[","");
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
                        cartList.add(new ProductInCartDTO(idProduct-1,size,quantity));
                    }

                }




                List<Integer> ordersIds= new ArrayList<>();

                if (!orders.equals("[]")){
                    String firstReplace= orders.replace("[","");
                    String secondReplace= firstReplace.replace("]","");

                    Scanner scanner= new Scanner(secondReplace);
                    scanner.useDelimiter(",");
                    //   String oneProduct= scanner.next();
                    while(scanner.hasNext()){
                        String idOrderString= scanner.next();
                        String trimmed=idOrderString.trim();
                        int idOrder= Integer.parseInt(trimmed);
                        ordersIds.add(idOrder);
                    }
                }
                System.out.println("Id zamowien po dodaniu: " + ordersIds);
                Customer.customers.put(loginHashed, new Customer(loginHashed,firstName,lastName,address,telNumber, creditsDouble,email,cartList,ordersIds));
                System.out.println("Koszyk: "+Customer.customers.get(loginHashed).getCurrentCart());
                System.out.println("Lista id zamowien: "+Customer.customers.get(loginHashed).getOrdersIds());

            }
        }
    }
    public static LinkedHashMap<Size, Integer> parseSizeQuantityData(String data) {
        LinkedHashMap<Size, Integer> sizeQuantityMap = new LinkedHashMap<>();
        String[] sizeQuantityPairs = data.split(",\\s*");

        for (String pair : sizeQuantityPairs) {
            String[] parts = pair.split("=");
            Size size = Size.valueOf(parts[0]);
            int quantity = Integer.parseInt(parts[1]);
            sizeQuantityMap.put(size, quantity);
        }

        return sizeQuantityMap;
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
    public static void savePasswordChangesToFile(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(accountsFileName));
            bufferedWriter.write("login;password;role -> password hash= (login+password).hash\n");
            bufferedWriter.write("login is unique for every user so accounts with same passwords have different hash\n");
            for (Map.Entry<String, PasswordRoleDTO> entry : accounts.entrySet()) {
                String key = entry.getKey();
                PasswordRoleDTO value = entry.getValue();
                Role role=value.getRole();
                String roleHash= String.valueOf(role.toString().hashCode());
                bufferedWriter.write(key+";"+value.getPassword()+";"+roleHash);
                bufferedWriter.write("\n");
            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void saveCustomersChangesToFile(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(customersFileName));
            bufferedWriter.write("loginHash;firstName;lastName;address;tel;credits;email;currentCart;orders\n");
            for (Map.Entry<String, Customer> customer : Customer.customers.entrySet()) {
                String key = customer.getKey();
                Customer customerValue = customer.getValue();

                bufferedWriter.write(key+"!"
                        + customerValue.getFirstName() +"!"
                        + customerValue.getLastName() + "!"
                        + customerValue.getAddress() + "!"
                        + customerValue.getTel() + "!"
                        + customerValue.getCredits() + "!"
                        + customerValue.getEmail() + "!"
                        + customerValue.getCurrentCart() + "!"
                        + customerValue.getOrdersIds());
                bufferedWriter.write("\n");
            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
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
        public static void accountScreenCustomer(Customer customer, String loginEntered){
            secondPanel = new JPanel();

            secondPanel.setLayout(new GridLayout(8, 2, 10, 10));
            frame.setTitle("Account");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            JLabel firstNameLabel= new JLabel("First name:");
            JTextField firstNameTextField= new JTextField();
            if (customer.getFirstName()==null){
                firstNameTextField.setText("");
            }else {
                firstNameTextField.setText(!customer.getFirstName().equals("null") ? customer.getFirstName() : "");
            }


            JLabel lastNameLabel= new JLabel("Last name:");
            JTextField lastNameTextField= new JTextField();

            if (customer.getLastName()==null){
                lastNameTextField.setText("");
            }else {
                lastNameTextField.setText(!customer.getLastName().equals("null") ? customer.getLastName() : "");
            }


            JLabel addressLabel= new JLabel("Address:");
            JTextField addressTextField= new JTextField();
            if (customer.getAddress()==null){
                addressTextField.setText("");
            }else {
                addressTextField.setText(!customer.getAddress().equals("null") ? customer.getAddress() : "");
            }


            JLabel telLabel= new JLabel("Tel:");
            JTextField telTextField= new JTextField();
            if (customer.getTel()==0){
                telTextField.setText("");
            }else {
                telTextField.setText(String.valueOf(customer.getTel()));
            }


            JLabel emailLabel= new JLabel("E-mail:");
            JTextField emailTextField= new JTextField();
            if (customer.getEmail()==null){
                emailTextField.setText("");
            }else {
                emailTextField.setText(!customer.getEmail().equals("null") ? customer.getEmail() : "");
            }



            JLabel newPasswordLabel= new JLabel("New password:");
            JPasswordField newPasswordField= new JPasswordField();

            JLabel confirmNewPasswordLabel= new JLabel("Confirm password:");
            JPasswordField confirmNewPasswordField= new JPasswordField();

            JButton backButton= backToMenuClient(customer, loginEntered);

            JButton saveButton= new JButton("Save changes");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!firstNameTextField.getText().equals(customer.getFirstName())){
                        customer.setFirstName(firstNameTextField.getText());
                    }
                    if (!lastNameTextField.getText().equals(customer.getLastName())){
                        customer.setLastName(lastNameTextField.getText());
                    }
                    if (!addressTextField.getText().equals(customer.getAddress())){
                        customer.setAddress(addressTextField.getText());
                    }
                    if (!emailTextField.getText().equals(customer.getEmail())){
                        customer.setEmail(emailTextField.getText());
                    }
                    try {

                        if (!telTextField.getText().equals("") && Integer.parseInt(telTextField.getText()) != (customer.getTel())) {
                            String text = telTextField.getText();
                            String enteredPhone = text.trim();
                            if (!enteredPhone.matches("\\d{9}")) {
                                throw new Exception("Type 9 digits in phone number field!");
                            }
                            customer.setTel(Integer.parseInt(enteredPhone));

                        }
                        if (newPasswordField.getPassword().length!=0 || confirmNewPasswordField.getPassword().length!=0){
                            if (newPasswordField.getPassword().length<_minPasswordLength ){
                                throw new Exception("Too short password! Minimum 7 characters!");
                            }
                            if (!Arrays.equals(newPasswordField.getPassword(), confirmNewPasswordField.getPassword())){
                                throw new Exception("Passwords are not the same!");
                            }
                            //ustawianie nowego hasla
                            char[] passwordChars = newPasswordField.getPassword();
                            String enteredPassword= new String(passwordChars);
                            String toHash=loginEntered+enteredPassword;
                            long newPasswordLong= toHash.hashCode();
                            String newPassword=String.valueOf(newPasswordLong);
                            customer.setPassword(newPassword);
                            long enteredLoginHash= loginEntered.hashCode();
                            String enteredLoginStringhash= String.valueOf(enteredLoginHash);
                            accounts.replace(enteredLoginStringhash,new PasswordRoleDTO(newPassword, Role.CLIENT));
//                            // zapisanie zmian do pliku
//                            savePasswordChangesToFile();
                            // moze uzyc jeszcze listy customers jesli bedzie do czegos potrzebna zeby w niej zmienialo
                            // sie haslo ale powinno byc zmienione skoro w niej sa referencje a zmieniam haslo
                            // na danym obiekcie ktorego referencja jest w tej liscie
                        }
                        JOptionPane.showMessageDialog(frame, "Saved changes successfully!");
                    }catch (NumberFormatException numberFormatException){
                        JOptionPane.showMessageDialog(frame, "Type 9 digits in phone number field!", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exception){
                        JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            });

            secondPanel.add(firstNameLabel);
            secondPanel.add(firstNameTextField);
            secondPanel.add(lastNameLabel);
            secondPanel.add(lastNameTextField);
            secondPanel.add(addressLabel);
            secondPanel.add(addressTextField);
            secondPanel.add(telLabel);
            secondPanel.add(telTextField);
            secondPanel.add(emailLabel);
            secondPanel.add(emailTextField);
            secondPanel.add(newPasswordLabel);
            secondPanel.add(newPasswordField);
            secondPanel.add(confirmNewPasswordLabel);
            secondPanel.add(confirmNewPasswordField);
            secondPanel.add(backButton);
            secondPanel.add(saveButton);


            frame.getContentPane().removeAll();
            frame.getContentPane().add(secondPanel);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            frame.setVisible(true);

        }
        public static void cartScreen(Customer customer, String loginEntered){
            secondPanel = new JPanel();
            secondPanel.setLayout(new BorderLayout());


            frame.setTitle("Cart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            JList<ProductInCartDTO> productsInCart= new JList<>();
            DefaultListModel<ProductInCartDTO> listModel= new DefaultListModel<>();
            JLabel label= new JLabel("Your cart: ");
            secondPanel.add(label,BorderLayout.NORTH);



            // ustawic label w lepszym miejscu i zeby sie aktualizowal odrazu po usunieciu produktu
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            double cost=calculateCartCost(customer.getCurrentCart());
            String formattedCredits = decimalFormat.format(cost);
            JLabel totalCostLabel= new JLabel("Cost: " + formattedCredits+"PLN");
            secondPanel.add(totalCostLabel,BorderLayout.EAST);

            //JSplitPane splitPane= new JSplitPane();


            // usunac potem, tylko do celow testowych
//            ProductWithSizeAndQtity product1= new ProductWithSizeAndQtity(new Product(Category.HOODIE,"Bluza rozpinana","Nike",249.99,"Wygodna sportowa bluza"));
//            product1.addSizeAndQuantity(Size.M,5);
//            product1.addSizeAndQuantity(Size.L,3);
//            customer.addToCart(product1);
//            ProductWithSizeAndQtity product2= new ProductWithSizeAndQtity(new Product(Category.PANTS,"Spodnie","Adidas",99.99,"Cienkie i przewiewne"));
//            product2.addSizeAndQuantity(Size.S,2);
//            product2.addSizeAndQuantity(Size.M,4);
//            customer.addToCart(product2);
//            ProductInCartDTO p1= new ProductInCartDTO(1-1,Size.M,2);
//            ProductInCartDTO p2= new ProductInCartDTO(2-1,Size.L,1);
//
//            customer.addToCart(p1);
//            customer.addToCart(p2);


            for( ProductInCartDTO productInCartDTO : customer.getCurrentCart()){
                listModel.addElement(productInCartDTO);
            }
            productsInCart.setModel(listModel);





            secondPanel.add(new JScrollPane(productsInCart),BorderLayout.CENTER);


           // splitPane.setLeftComponent(new JScrollPane(productsInCart));

            JPanel buttonsPanel= new JPanel();
            JButton backButton= backToMenuClient(customer, loginEntered);
            buttonsPanel.add(backButton);

            JButton removeProductFromCartButton= new JButton("Remove");

            removeProductFromCartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                        try {
                            if (productsInCart.getSelectedValue()==null){
                                throw new Exception("Select a product!");
                            }
                            ProductInCartDTO productToRemove= productsInCart.getSelectedValue();
                            customer.getCurrentCart().remove(productToRemove);
                            listModel.remove(productsInCart.getSelectedIndex());
                            JOptionPane.showMessageDialog(frame, "Product has been removed!");
                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            double cost=calculateCartCost(customer.getCurrentCart());
                            String formattedCredits = decimalFormat.format(cost);
                            totalCostLabel.setText("Cost: " +formattedCredits+"PLN");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame,ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                        }

                }
            });
            buttonsPanel.add(removeProductFromCartButton);

            JButton orderButton= new JButton("Order");
            orderButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // weryfikacja danych osobowych i platnosci.
                    try {
                        placeAnOrder(customer,loginEntered,customer.getCurrentCart());
                        listModel.removeAllElements();
                        double cost=calculateCartCost(customer.getCurrentCart());
                        String formattedCredits = decimalFormat.format(cost);
                        totalCostLabel.setText("Cost: " +formattedCredits+ "PLN");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonsPanel.add(orderButton);

            secondPanel.add(buttonsPanel,BorderLayout.SOUTH);

            // splitPane.setBottomComponent(buttonsPanel);
            // panel.add(label);
           // splitPane.setRightComponent(panel);

           // secondPanel.add(splitPane);

            frame.getContentPane().removeAll();
            frame.getContentPane().add(secondPanel);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            frame.setVisible(true);

        }
        public static double calculateCartCost(List<ProductInCartDTO> productInCartDTOS){
        double totalCost=0;
        for (ProductInCartDTO product : productInCartDTOS){
            System.out.println("Calculating price for: "+ product);
            double pricePerOne= Product.allProducts.get(product.getIdProduct()).getPrice();
            int quantity= product.getQuantity();

            totalCost+=pricePerOne*quantity;
        }
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String formattedPrice = decimalFormat.format(totalCost);
            String noCommaPrice= formattedPrice.replace(",",".");
            double formattedCost=Double.parseDouble(noCommaPrice);

        return formattedCost;
        }
        public static void placeAnOrder(Customer customer, String loginEntered, List<ProductInCartDTO> productsInCart) throws Exception {
        double totalCost=0;

        if (customer.getFirstName()==null){
            throw new Exception("Fill first name in the Account section.");
        }
        if (customer.getLastName()==null){
            throw new Exception("Fill last name in the Account section.");
        }
        if (customer.getAddress()==null){
            throw new Exception("Fill address in the Account section.");
        }
        if (customer.getTel()==0){
            throw new Exception("Fill phone number in the Account section.");
        }
        if (customer.getEmail()==null){
            throw new Exception("Fill e-mail in the Account section.");
        }
            LinkedHashMap<Integer, ProductInCartDTO> productsToOrder= new LinkedHashMap<>();
            if (productsInCart.isEmpty()){
                throw new Exception("Your cart is empty!");
            }

            System.out.println("Weryfikacja danych osobowych i koszyka ze nie jest pusty GIT");
        for (ProductInCartDTO product : productsInCart){
            Optional<ProductWithSizeAndQtity> optionalProduct= ProductWithSizeAndQtity.availableProductsWithSizesAndQtity
                    .stream()
                    .filter(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getId()-1==product.getIdProduct())
                    .findFirst();
            ProductWithSizeAndQtity p2Temp=null;
            if (optionalProduct.isPresent()){
                p2Temp=optionalProduct.get();
            }
            System.out.println("Produkt z koszyka id-1: " + product.getIdProduct());
            System.out.println("Produkt z magazynu id-1: " + p2Temp.getProduct().getId());

            if (!ProductWithSizeAndQtity.availableProductsWithSizesAndQtity.contains(p2Temp)){
                throw new Exception("Product: " +  Product.allProducts.get(product.getIdProduct())+ "  is unavailable!");
            }
            // dla kazdego produktu z koszyka:
            // 1. pobieramy jego id
            // 2. pobieramy rozmiar i ilosc
            // tworzymy Obiekt ProductWithSizeAndQtity o takim id jak ten produkt z koszyka
            // tworzymy mape DOSTEPNYCH NA MAGAZYNIE  rozmiar ilosc uzywajac tego obiektu z magazynu
            // jesli ta mapa dostepnych zawiera klucz o danym SIZE to sprawdzam czy jest ilosc dostepna
            // gdy wszystkie warunki sa spelnione dodajemy do LinkedHashMap obiekt klucz- ID PRODUKTU Z KOSZYKA, wartosc- DTO obiekt o Size i quantity
            // na koniec przechodze przez ta liste i zmniejszam dostepne produkty o podanym rozmiarze i podanej ilosci
            System.out.println("Przed pobieraniem id");
           // ProductWithSizeAndQtity productFromWarehouse= ProductWithSizeAndQtity.availableProductsWithSizesAndQtity.stream().filter(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getId()==product.getIdProduct()).findFirst().get();
            ProductWithSizeAndQtity productFromWarehouse=p2Temp;
            // na tej linii wywala blad
            System.out.println("Pobrany produkt: "+ productFromWarehouse);
            // trzeba zaseedowac baze produktami o ilosci i rozmiarach
            System.out.println("Po pobieraniu id");
            Size size= product.getSize();
            int quantity= product.getQuantity();
            LinkedHashMap<Size, Integer> sizesAndQuantitiesAvailable= productFromWarehouse.getSizesAndQuantitiesMap();


            if (sizesAndQuantitiesAvailable.containsKey(size)){

                int availableQuantity= sizesAndQuantitiesAvailable.get(size);

                if (quantity>availableQuantity){

                 //   ProductWithSizeAndQtity.availableProductsWithSizesAndQtity.get(product.getIdProduct()).decreaseProductQuantity(size,quantity);
                    throw new NotEnoughProductsException("For product: " + product.getProductName() + " this amount is unavailable at the moment!");
                }
                System.out.println("Przed wywolaniem x");
                // powiekszamy koszt calkowity o ilosc produktow * cena za sztuke
                totalCost+=ProductWithSizeAndQtity.availableProductsWithSizesAndQtity.get(Product.allProducts.get(product.getIdProduct()).getId()-1).getProduct().getPrice()*quantity;
                System.out.println("Po wywolaniu x");
            } else {
                throw new UnavailableException("For product: " + product.getProductName() + ", size: " + size + " is unavailable at the moment!");
            }
            if (totalCost>customer.getCredits()){
                throw new Exception("Not enough credits! Buy more in the Wallet section.");
            }
            ProductInCartDTO productTemp= new ProductInCartDTO(product.getIdProduct(),size,quantity);
            productsToOrder.put(product.getIdProduct(),productTemp);
        }
                List<ProductInCartDTO> orderedProducts= new ArrayList<>();
            for (Map.Entry<Integer, ProductInCartDTO> entry : productsToOrder.entrySet()) {
                Integer key = entry.getKey();
                ProductInCartDTO value = entry.getValue();
                orderedProducts.add(value);
                ProductWithSizeAndQtity.availableProductsWithSizesAndQtity.get(key).decreaseProductQuantity(value.getSize(),value.getQuantity());
            }

        Order order= new Order(orderedProducts);
            customer.getOrdersIds().add(order.getIdOrder());
            customer.getOrders().add(order);

            // zmniejszenie ilosci kredytow o kwote zamowienia
            customer.setCredits(customer.getCredits()-order.calculateCost());
            customer.getCurrentCart().clear();
            JOptionPane.showMessageDialog(frame,"Order nr " + order.getIdOrder()+ " has been placed!", "Order", JOptionPane.PLAIN_MESSAGE);

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