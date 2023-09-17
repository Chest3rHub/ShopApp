package GUI;

import DTOs.PasswordRoleDTO;
import DTOs.ProductInCartDTO;
import Exceptions.NotEnoughProductsException;
import Exceptions.UnavailableException;
import Models.Customers.Customer;
import Models.Employees.*;
import Models.Order;
import Models.Products.Category;
import Models.Products.Product;
import Models.Products.ProductWithSizeAndQtity;
import Models.Products.Size;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopGUI extends JFrame {

    public static final Font _FONT = new JLabel().getFont();
    public static final int _minPasswordLength=7;
    public static final int _minLoginLength=5;

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
        loggedOutScreen();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                /**
                 * This method saves customers, products, orders changes to file
                 * when window is closed
                 */
                try {
                    savePasswordChangesToFile();
                    saveCustomersChangesToFile();
                    Order.saveOrdersToFile();
                    Consultant.saveConsultantFeedbackToFile();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void changeToLoginScreen() {
        /**
         * This method changes frame to login
         */
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());

        frame.setTitle("Login to Shop App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        secondPanel.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel loginLabel = new JLabel("Login:");
        JTextField loginField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Sign in");

        frame.getRootPane().setDefaultButton(loginButton);

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
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
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
        /**
         * This method changes frame to logged out screen with register and login buttons.
         */
        BorderLayout borderLayout= new BorderLayout();
        JPanel mainPanel = new JPanel(borderLayout);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setTitle("Shop App");
        frame.setLayout(new BorderLayout());

        mainPanel.setLayout(borderLayout);

        frame.add(mainPanel,BorderLayout.CENTER);

        JLabel welcomeLabel= new JLabel("Welcome to Shop App!",SwingConstants.CENTER);
        welcomeLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,19));

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        ImageIcon imageIcon= new ImageIcon("src/Graphics/shoppingCart.png");
        frame.setIconImage(imageIcon.getImage());

        JPanel buttonPanel = new JPanel();

        JButton loginButton = new JButton("Sign in");

        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");

        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel,BorderLayout.CENTER);

        JLabel creditsLabel= new JLabel("~ by Szymon Sawicki :)",SwingConstants.SOUTH_EAST);

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

        frame.setLocation(x, y);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainPanel);
        frame.setTitle("Shop App");
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        frame.setVisible(true);
    }


    public static void adminLoggedIn() {
        /**
         * This method changes frame to admin logged in
         */
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("ADMIN");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secondPanel.add(welcomeLabel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());


        JButton button1 = new JButton("Wyswietl produkty");
        JButton button2 = new JButton("Wyswietl produkty");
        JButton button3 = new JButton("Dodaj produkt");
        JButton consultansButton= new JButton("Consultants");

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
        consultansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeScreenToConsultantsAdmin();
            }
        });

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(logOutButton);
        buttonPanel.add(consultansButton);
        secondPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.setTitle("Menu: ADMIN");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public static void changeScreenToConsultantsAdmin(){
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(600, 500);

        JLabel consultantsLabel = new JLabel("Consultants: ");
        consultantsLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,16));
        consultantsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secondPanel.add(consultantsLabel, BorderLayout.NORTH);

        DefaultListModel<Consultant> consultantModel = new DefaultListModel<>();
        JList<Consultant> consultantJList = new JList<>(consultantModel);
        for (Consultant consultant : Consultant.consultantList) {
            consultantModel.addElement(consultant);
        }
        consultantJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Consultant) {
                    value = "ID: " + ((Consultant) value).getId()
                            + ", NAME: " + ((Consultant) value).getFirstName()
                            + ", SURNAME: " + ((Consultant) value).getLastName()
                            + ", HIRE DATE: " + ((Consultant) value).getHireDate();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        JTextArea consultantInfoTextArea = new JTextArea();
        consultantInfoTextArea.setEditable(false);
        consultantInfoTextArea.setWrapStyleWord(true);
        consultantInfoTextArea.setLineWrap(true);

        Consultant.setAverageRatingForAllConsultants();
        consultantJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!consultantJList.isSelectionEmpty()){
                    Consultant consultant = consultantJList.getSelectedValue();
                    String averageText="AVERAGE RATING: ";
                    String ratingText="";
                    String feedbackText="FEEDBACK: \n";
                    if (consultant.getFeedbackFromCustomerList().isEmpty()){
                        ratingText="No feedback yet :(";
                    } else {
                        ratingText+=consultant.getAverageRating();
                    }
                    averageText+=ratingText;
                    for (Feedback feedback : consultant.getFeedbackFromCustomerList()){
                        feedbackText+=feedback + "\n";
                    }
                    averageText+="\n\n";
                    String finalText= averageText+ feedbackText;
                    consultantInfoTextArea.setText(finalText);
                }else {
                    consultantInfoTextArea.setText("");
                }

            }
        });
        JPanel listAndInfoPanel = new JPanel(new BorderLayout());
        listAndInfoPanel.add(new JScrollPane(consultantJList), BorderLayout.CENTER);

        listAndInfoPanel.add(consultantInfoTextArea, BorderLayout.SOUTH);
        JButton backButton = backToMenuAdmin();

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);

        JPanel orderByPanel= new JPanel(new GridLayout(8,1));
        JLabel sortLabel= new JLabel("ORDER BY: ");

        JRadioButton noneButton= new JRadioButton("None");
        noneButton.setSelected(true);
        JRadioButton averageRatingAsc= new JRadioButton("Average rating asc");
        JRadioButton averageRatingDesc= new JRadioButton("Average rating desc");
        JRadioButton hireDateAscButton= new JRadioButton("Hire date asc");
        JRadioButton hireDateDescButton= new JRadioButton("Hire date desc");
        JRadioButton lastNameAscButton= new JRadioButton("Last name A -> Z");
        JRadioButton lastNameDescButton= new JRadioButton("Last name Z -> A");

        ButtonGroup buttonGroup= new ButtonGroup();

        buttonGroup.add(noneButton);
        buttonGroup.add(averageRatingAsc);
        buttonGroup.add(averageRatingDesc);
        buttonGroup.add(hireDateAscButton);
        buttonGroup.add(hireDateDescButton);
        buttonGroup.add(lastNameAscButton);
        buttonGroup.add(lastNameDescButton);

        noneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultantJList.clearSelection();
                consultantModel.removeAllElements();
                for (Consultant consultant : Consultant.consultantList){
                    consultantModel.addElement(consultant);
                }
            }
        });

        averageRatingAsc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultantJList.clearSelection();

              //  consultantModel.removeAllElements();
              //  List<Consultant> orderedBy= Consultant.getConsultantsOrderedByAverageRatingAsc(Consultant.consultantList);
              //  consultantModel.addAll(orderedBy);
            }
        });

        averageRatingDesc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultantJList.clearSelection();

              //  consultantModel.removeAllElements();
              //  List<Consultant> orderedBy= Consultant.getConsultantsOrderedByHireDateDesc(Consultant.consultantList);
              //  consultantModel.addAll(orderedBy);
            }
        });

        hireDateAscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultantJList.clearSelection();
                consultantModel.removeAllElements();
                List<Consultant> orderedBy= Consultant.getConsultantsOrderedByHireDateAsc(Consultant.consultantList);
                consultantModel.addAll(orderedBy);
            }
        });

        hireDateDescButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultantJList.clearSelection();
                consultantModel.removeAllElements();
                List<Consultant> orderedBy= Consultant.getConsultantsOrderedByHireDateDesc(Consultant.consultantList);
                consultantModel.addAll(orderedBy);
            }
        });

        lastNameAscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultantJList.clearSelection();

                consultantModel.removeAllElements();
                List<Consultant> orderedBy= Consultant.getConsultantsOrderedByLastNameAsc(Consultant.consultantList);
                consultantModel.addAll(orderedBy);
            }
        });

        lastNameDescButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consultantJList.clearSelection();

                consultantModel.removeAllElements();
                List<Consultant> orderedBy= Consultant.getConsultantsOrderedByLastNameDesc(Consultant.consultantList);
                consultantModel.addAll(orderedBy);
            }
        });
        orderByPanel.add(sortLabel);
        orderByPanel.add(noneButton);

        orderByPanel.add(averageRatingAsc);
        orderByPanel.add(averageRatingDesc);
        orderByPanel.add(hireDateAscButton);
        orderByPanel.add(hireDateDescButton);
        orderByPanel.add(lastNameAscButton);
        orderByPanel.add(lastNameDescButton);


        // na poczatku wyliczyc srednia ocen dla wszystkich pracownikow (jest metoda juz)
        // pozniej stworzyc metode ktora bedzie sortowac ta liste jak juz bedzie wyliczona srednia
        // i zmienic liczbe w gridlayout bo jest za malo wierszy


        secondPanel.add(listAndInfoPanel, BorderLayout.CENTER);
        secondPanel.add(buttonPanel, BorderLayout.SOUTH);
        secondPanel.add(orderByPanel,BorderLayout.EAST);

        frame.setTitle("Consultants");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public static void managerLoggedIn(){

    }
    public static void clientLoggedIn(Customer customer, String enteredLogin){
        /**
         * This method changes frame to client logged in
         * @param customer Customer value attached to entered login
         * @param enteredLogin String login value entered while logging in
         */
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(450,350);
        
        JLabel welcomeLabel= new JLabel("Welcome " + enteredLogin + "!");
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JLabel creditsLabel= new JLabel("Credits: " + customer.getCredits());

        JPanel topPanel= new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        secondPanel.add(topPanel,BorderLayout.NORTH);

        JPanel downPanel= new JPanel(new FlowLayout());

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
                helpMenuClient(customer,enteredLogin);
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
    public static void helpMenuClient(Customer customer, String loginEntered){
        /**
         * This method displays help menu with option to call one of consultants
         * @param customer Customer value attached to entered login
         * @param loginEntered String value of entered login
         */
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(450,350);

        JButton backButton= backToMenuClient(customer,loginEntered);

        JButton callButton= new JButton("Call");

        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callingConsultantScreen(customer,loginEntered);
            }
        });

        JPanel buttonPanel= new JPanel(new FlowLayout());

        JLabel contactLabel= new JLabel(" CONTACT: ");
        contactLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,20));

        JLabel emailLabel= new JLabel(" e-mail:  help@email.com    ");
        emailLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,15));

        JLabel telLabel= new JLabel(" tel:       +48 123 123 123    ");
        telLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,15));

        JPanel labelPanel= new JPanel(new GridLayout(3,1,10,10));

        labelPanel.add(contactLabel);
        labelPanel.add(emailLabel);
        labelPanel.add(telLabel);

        buttonPanel.add(backButton);
        buttonPanel.add(callButton);

        secondPanel.add(labelPanel,BorderLayout.CENTER);
        secondPanel.add(buttonPanel,BorderLayout.SOUTH);

        frame.setTitle("Help");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.pack();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public static void callingConsultantScreen(Customer customer, String loginEntered){
        /**
         * This method displays calling to a random consultant
         * @param customer Customer value attached to entered login
         * @param loginEntered String value of entered login
         */
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(350,250);


        JLabel callingLabel= new JLabel("Calling...");
        callingLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,25));
        callingLabel.setHorizontalAlignment(JLabel.CENTER);

        secondPanel.add(callingLabel,BorderLayout.CENTER);
        frame.setTitle("Help");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
      //  frame.pack();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        Timer timer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Consultant consultant= Consultant.getRandomConsultant();
                callFinishedScreen(customer,loginEntered, consultant);
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    public static void callFinishedScreen(Customer customer, String loginEntered, Consultant consultant){
        /**
         * This method displays screen after the call with option to leave feedback
         * @param customer Customer value attached to entered login
         * @param loginEntered String value of entered login
         * @param consultant Consultant who joined the call
         */
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(350,200);

        JButton backButton= backToMenuClient(customer,loginEntered);

        JButton feedbackButton= new JButton("Feedback");

        feedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leaveFeedbackToConsultantMenu(customer,loginEntered,consultant);
            }
        });



        JPanel buttonsPanel= new JPanel(new FlowLayout());

        String text="Your Consultant: " + consultant.getFirstName() + " " + consultant.getLastName();
        JLabel yourConsultantLabel= new JLabel(text);
        yourConsultantLabel.setHorizontalAlignment(JLabel.CENTER);
        yourConsultantLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,16));


        buttonsPanel.add(backButton);
        buttonsPanel.add(feedbackButton);
        JPanel textPanel= new JPanel();

        JLabel thankYouLabel= new JLabel("Thank You for calling!");
        thankYouLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,20));

        textPanel.add(thankYouLabel);
        textPanel.add(yourConsultantLabel);

        secondPanel.add(textPanel,BorderLayout.CENTER);

        secondPanel.add(buttonsPanel,BorderLayout.SOUTH);

        frame.setTitle("Help");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        //  frame.pack();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
    public static void leaveFeedbackToConsultantMenu(Customer customer, String loginEntered, Consultant consultant){
        /**
         * This method displays leaving feedback screen
         * @param customer Customer value attached to entered login
         * @param loginEntered String value of entered login
         * @param consultant Consultant who joined the call
         */
        secondPanel = new JPanel();
        secondPanel.setLayout(new BorderLayout());
        frame.setSize(350,200);

        JPanel buttonsPanel= new JPanel(new FlowLayout());

        JButton backButton= backToMenuClient(customer,loginEntered);

        JPanel radioButtonsPanel= new JPanel();
        ButtonGroup buttonGroup= new ButtonGroup();
        JLabel commentLabel= new JLabel("Comment:");
        commentLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,20));
        JTextField commentTextField= new JTextField();
        commentTextField.setMinimumSize(new Dimension(30,10));
        commentTextField.setEnabled(true);


        JPanel commentPanel= new JPanel(new GridLayout(1,2));
        commentPanel.add(commentLabel);
        commentPanel.add(commentTextField);

        JRadioButton oneRadioButton= new JRadioButton("1");
        JRadioButton twoRadioButton= new JRadioButton("2");
        JRadioButton threeRadioButton= new JRadioButton("3");
        JRadioButton fourRadioButton= new JRadioButton("4");
        JRadioButton fiveRadioButton= new JRadioButton("5");

        buttonGroup.add(oneRadioButton);
        buttonGroup.add(twoRadioButton);
        buttonGroup.add(threeRadioButton);
        buttonGroup.add(fourRadioButton);
        buttonGroup.add(fiveRadioButton);

        radioButtonsPanel.add(oneRadioButton);
        radioButtonsPanel.add(twoRadioButton);
        radioButtonsPanel.add(threeRadioButton);
        radioButtonsPanel.add(fourRadioButton);
        radioButtonsPanel.add(fiveRadioButton);

        JButton sendButton= new JButton("Send");

        buttonsPanel.add(backButton);
        buttonsPanel.add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if (commentTextField.getText().isEmpty() || commentTextField.getText().equals("")){
                         throw new Exception("Fill in comment section");
                    }
                    if (!(oneRadioButton.isSelected() || twoRadioButton.isSelected() || threeRadioButton.isSelected() || fourRadioButton.isSelected() || fiveRadioButton.isSelected())){
                         throw new Exception("Choose a rating first!");
                    }
                    String comment= commentTextField.getText();
                    Rating rating=null;
                    if (oneRadioButton.isSelected()){
                        rating=Rating.ONE;
                    } else if ( twoRadioButton.isSelected()){
                        rating= Rating.TWO;
                    } else if (threeRadioButton.isSelected()){
                        rating= Rating.THREE;
                    } else if (fourRadioButton.isSelected()){
                        rating= Rating.FOUR;
                    } else if (fiveRadioButton.isSelected()){
                        rating= Rating.FIVE;
                    }
                    LocalDate localDate= LocalDate.now();
                    Feedback feedback= new Feedback(rating,comment,localDate);
                    consultant.addFeedback(feedback);
                    JOptionPane.showMessageDialog(frame,"Feedback successfully sent!","Feedback", JOptionPane.PLAIN_MESSAGE);
                    clientLoggedIn(customer,loginEntered);
                }catch(Exception exception){
                    JOptionPane.showMessageDialog(frame,exception.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JPanel allComponentsPanel= new JPanel(new GridLayout(4,1));

        JLabel rateLabel= new JLabel("Rate: ");
        rateLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,20));

        allComponentsPanel.add(commentLabel);
        allComponentsPanel.add(commentTextField);
        allComponentsPanel.add(rateLabel);
        allComponentsPanel.add(radioButtonsPanel);

        secondPanel.add(allComponentsPanel,BorderLayout.CENTER);
        secondPanel.add(buttonsPanel,BorderLayout.SOUTH);

        frame.setTitle("Feedback");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        //  frame.pack();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }



    public static void productsMenuClient(Customer customer, String loginEntered){
        /**
         * This method changes frame to products Menu for client
         * @param customer Customer value attached to entered login
         * @param loginEntered String login value entered while logging in
         */
        frame.setSize(575,400);
        secondPanel = new JPanel(new BorderLayout());

        secondPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        DefaultListModel<ProductWithSizeAndQtity> productModel = new DefaultListModel<>();
        JList<ProductWithSizeAndQtity> productList = new JList<>(productModel);

        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                productModel.addElement(product);
            }
        }
        JPanel comboBoxPanel= new JPanel(new GridLayout(23,1));

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

        JLabel sortLabel= new JLabel("SORT: ");
        comboBoxPanel.add(sortLabel);

        JCheckBox orderByPriceAscendingBox= new JCheckBox("Price ascending");
        comboBoxPanel.add(orderByPriceAscendingBox);

        JCheckBox orderByPriceDescendingBox= new JCheckBox("Price descending");
        comboBoxPanel.add(orderByPriceDescendingBox);

        JLabel categoryLabel= new JLabel("CATEGORY: ");
        comboBoxPanel.add(categoryLabel);

        JRadioButton allRadioButton= new JRadioButton("All");
        comboBoxPanel.add(allRadioButton);
        allRadioButton.setSelected(true);

        JRadioButton accessoriesRadioButton= new JRadioButton("Accessories");
        comboBoxPanel.add(accessoriesRadioButton);

        JRadioButton hoodieRadioButton= new JRadioButton("Hoodie");
        comboBoxPanel.add(hoodieRadioButton);

        JRadioButton pantsRadioButton= new JRadioButton("Pants");
        comboBoxPanel.add(pantsRadioButton);

        JRadioButton shirtRadioButton= new JRadioButton("Shirt");
        comboBoxPanel.add(shirtRadioButton);

        JRadioButton socksRadioButton= new JRadioButton("Socks");
        comboBoxPanel.add(socksRadioButton);

        ButtonGroup categoryButtons= new ButtonGroup();

        categoryButtons.add(allRadioButton);
        categoryButtons.add(accessoriesRadioButton);
        categoryButtons.add(hoodieRadioButton);
        categoryButtons.add(pantsRadioButton);
        categoryButtons.add(shirtRadioButton);
        categoryButtons.add(socksRadioButton);

        allRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (allRadioButton.isSelected()){
                    if (orderByPriceAscendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }

                        if (orderByPriceAscendingBox.isSelected()){
                            List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                            try{
                                resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);

                            }catch(UnavailableException unavailableException){
                                unavailableException.printStackTrace();
                            }
                            productModel.addAll(resultList);
                        }else {
                            productModel.addAll(listTemp);
                        }
                    }else if (orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }

                        if (orderByPriceDescendingBox.isSelected()){
                            List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                            try{
                                resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);

                            }catch(UnavailableException unavailableException){
                                unavailableException.printStackTrace();
                            }
                            productModel.addAll(resultList);
                        }else {
                            productModel.addAll(listTemp);
                        }
                    }else if (!orderByPriceAscendingBox.isSelected() && !orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }
                        productModel.addAll(listTemp);
                    }

                }
            }
        });
        accessoriesRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (accessoriesRadioButton.isSelected()){

                    if (orderByPriceAscendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    }else if (orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    } else {
                        productModel.removeAllElements();

                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (product.getProduct().getCategory().equals(Category.ACCESSORIES) && !product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }
                        productModel.addAll(listTemp);
                    }


                }
            }
        });

        pantsRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pantsRadioButton.isSelected()){

                    if (orderByPriceAscendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    }else if (orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    } else {
                        productModel.removeAllElements();

                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (product.getProduct().getCategory().equals(Category.PANTS) && !product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }
                        productModel.addAll(listTemp);
                    }


                }
            }
        });


        hoodieRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hoodieRadioButton.isSelected()){

                    if (orderByPriceAscendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    }else if (orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    } else {
                        productModel.removeAllElements();

                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (product.getProduct().getCategory().equals(Category.HOODIE) && !product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }
                        productModel.addAll(listTemp);
                    }


                }
            }
        });
        shirtRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shirtRadioButton.isSelected()){

                    if (orderByPriceAscendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    }else if (orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    } else {
                        productModel.removeAllElements();

                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (product.getProduct().getCategory().equals(Category.SHIRT) && !product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }
                        productModel.addAll(listTemp);
                    }


                }
            }
        });

        socksRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (socksRadioButton.isSelected()){

                    if (orderByPriceAscendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    }else if (orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                listTemp.add(product);
                            }
                        }
                        try {
                            List<ProductWithSizeAndQtity> resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);
                            productModel.addAll(resultList);
                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                    } else {
                        productModel.removeAllElements();

                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (product.getProduct().getCategory().equals(Category.SOCKS) && !product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }
                        productModel.addAll(listTemp);
                    }

                }
            }
        });

        orderByPriceAscendingBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    orderByPriceDescendingBox.setSelected(false);
                    List<ProductWithSizeAndQtity> previousList = Collections.list(productModel.elements());

                    productModel.removeAllElements();
                    List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                    for (ProductWithSizeAndQtity product : previousList){
                        if (!product.getSizesAndQuantitiesMap().isEmpty()){
                            listTemp.add(product);
                        }
                    }

                    if (orderByPriceAscendingBox.isSelected()){
                        List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                        try{
                            resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);

                        }catch(UnavailableException unavailableException){
                            unavailableException.printStackTrace();
                        }
                        productModel.addAll(resultList);
                    }else {
                        productModel.addAll(listTemp);
                    }


            }

        });

        orderByPriceDescendingBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderByPriceAscendingBox.setSelected(false);
                List<ProductWithSizeAndQtity> previousList = Collections.list(productModel.elements());

                productModel.removeAllElements();
                List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                for (ProductWithSizeAndQtity product : previousList){
                    if (!product.getSizesAndQuantitiesMap().isEmpty()){
                        listTemp.add(product);
                    }
                }

                if (orderByPriceDescendingBox.isSelected()){
                    List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                    try{
                        resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);

                    }catch(UnavailableException unavailableException){
                        unavailableException.printStackTrace();
                    }
                    productModel.addAll(resultList);
                }else {
                    productModel.addAll(listTemp);
                }

            }
        });



        JLabel filterLabel= new JLabel("FILTER BY SIZE: ");
        comboBoxPanel.add(filterLabel);

        JCheckBox anySizeCheckBox= new JCheckBox("ANY");
        anySizeCheckBox.setSelected(true);
        comboBoxPanel.add(anySizeCheckBox);

        JCheckBox xsCheckBox= new JCheckBox("XS");
        comboBoxPanel.add(xsCheckBox);

        JCheckBox sCheckBox= new JCheckBox("S");
        comboBoxPanel.add(sCheckBox);

        JCheckBox mCheckBox= new JCheckBox("M");
        comboBoxPanel.add(mCheckBox);

        JCheckBox lCheckBox= new JCheckBox("L");
        comboBoxPanel.add(lCheckBox);

        JCheckBox xlCheckBox= new JCheckBox("XL");
        comboBoxPanel.add(xlCheckBox);

        ButtonGroup sizeGroup= new ButtonGroup();
        sizeGroup.add(anySizeCheckBox);
        sizeGroup.add(xsCheckBox);
        sizeGroup.add(sCheckBox);
        sizeGroup.add(mCheckBox);
        sizeGroup.add(lCheckBox);
        sizeGroup.add(xlCheckBox);


        anySizeCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (anySizeCheckBox.isSelected()){
//                    xsCheckBox.setSelected(false);
//                    sCheckBox.setSelected(false);
//                    mCheckBox.setSelected(false);
//                    lCheckBox.setSelected(false);
//                    xlCheckBox.setSelected(false);

                    List<ProductWithSizeAndQtity> productsFromCategoryList= new ArrayList<>();
                    productModel.removeAllElements();

                    if (allRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (accessoriesRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (pantsRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (hoodieRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                productsFromCategoryList.add(product);
                            }
                        }

                    }else if (shirtRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (socksRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }

                    if (orderByPriceAscendingBox.isSelected()){

                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : productsFromCategoryList){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }

                        if (orderByPriceAscendingBox.isSelected()){
                            List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                            try{
                                resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(listTemp);

                            }catch(UnavailableException unavailableException){
                                unavailableException.printStackTrace();
                            }
                            productModel.addAll(resultList);
                        }else {
                            productModel.addAll(listTemp);
                        }
                    }else if (orderByPriceDescendingBox.isSelected()){
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : productsFromCategoryList){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }

                        if (orderByPriceDescendingBox.isSelected()){
                            List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                            try{
                                resultList= ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(listTemp);

                            }catch(UnavailableException unavailableException){
                                unavailableException.printStackTrace();
                            }
                            productModel.addAll(resultList);
                        }else {
                            productModel.addAll(listTemp);
                        }
                    }else if (!orderByPriceAscendingBox.isSelected() && !orderByPriceDescendingBox.isSelected()){
                        productModel.removeAllElements();
                        List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                        for (ProductWithSizeAndQtity product : productsFromCategoryList){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                listTemp.add(product);
                            }
                        }
                        productModel.addAll(listTemp);
                    }

                }else {
                    // do nothing :)
                }
            }
        });

        xsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xsCheckBox.isSelected()){
                    anySizeCheckBox.setSelected(false);
                    List<ProductWithSizeAndQtity> productsFromCategoryList= new ArrayList<>();
                    productModel.removeAllElements();
                    if (allRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (accessoriesRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (pantsRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (hoodieRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                productsFromCategoryList.add(product);
                            }
                        }

                    }else if (shirtRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (socksRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }
                    List<ProductWithSizeAndQtity> correctOrderList= new ArrayList<>();
                    if (orderByPriceAscendingBox.isSelected()){
                        try {
                            correctOrderList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    }else if (orderByPriceDescendingBox.isSelected()){
                        try {
                            correctOrderList=ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    } else if (!orderByPriceAscendingBox.isSelected() && !orderByPriceDescendingBox.isSelected()){
                        correctOrderList=productsFromCategoryList;
                    }
                    List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                    try {
                        resultList= ProductWithSizeAndQtity.getProductListBySize(Size.XS,correctOrderList);
                    } catch (UnavailableException ex) {
                        ex.printStackTrace();
                    }
                    productModel.addAll(resultList);
                }else {
                    // do nothing
                }

            }
        });


        sCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sCheckBox.isSelected()){
                    List<ProductWithSizeAndQtity> productsFromCategoryList= new ArrayList<>();
                    productModel.removeAllElements();
                    if (allRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (accessoriesRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (pantsRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (hoodieRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                productsFromCategoryList.add(product);
                            }
                        }

                    }else if (shirtRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (socksRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }
                    List<ProductWithSizeAndQtity> correctOrderList= new ArrayList<>();
                    if (orderByPriceAscendingBox.isSelected()){
                        try {
                            correctOrderList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    }else if (orderByPriceDescendingBox.isSelected()){
                        try {
                            correctOrderList=ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    } else if (!orderByPriceAscendingBox.isSelected() && !orderByPriceDescendingBox.isSelected()){
                        correctOrderList=productsFromCategoryList;
                    }
                    List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                    try {
                        resultList= ProductWithSizeAndQtity.getProductListBySize(Size.S,correctOrderList);
                    } catch (UnavailableException ex) {
                        ex.printStackTrace();
                    }
                    productModel.addAll(resultList);
                }else {
                    // do nothing
                }
            }
        });

        mCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mCheckBox.isSelected()){
                    List<ProductWithSizeAndQtity> productsFromCategoryList= new ArrayList<>();
                    productModel.removeAllElements();
                    if (allRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (accessoriesRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (pantsRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (hoodieRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                productsFromCategoryList.add(product);
                            }
                        }

                    }else if (shirtRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (socksRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }
                    List<ProductWithSizeAndQtity> correctOrderList= new ArrayList<>();
                    if (orderByPriceAscendingBox.isSelected()){
                        try {
                            correctOrderList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    }else if (orderByPriceDescendingBox.isSelected()){
                        try {
                            correctOrderList=ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    } else if (!orderByPriceAscendingBox.isSelected() && !orderByPriceDescendingBox.isSelected()){
                        correctOrderList=productsFromCategoryList;
                    }
                    List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                    try {
                        resultList= ProductWithSizeAndQtity.getProductListBySize(Size.M,correctOrderList);
                    } catch (UnavailableException ex) {
                        ex.printStackTrace();
                    }
                    productModel.addAll(resultList);
                }else {
                    // do nothing
                }
            }
        });

        lCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lCheckBox.isSelected()){
                    List<ProductWithSizeAndQtity> productsFromCategoryList= new ArrayList<>();
                    productModel.removeAllElements();
                    if (allRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (accessoriesRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (pantsRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (hoodieRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                productsFromCategoryList.add(product);
                            }
                        }

                    }else if (shirtRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (socksRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }
                    List<ProductWithSizeAndQtity> correctOrderList= new ArrayList<>();
                    if (orderByPriceAscendingBox.isSelected()){
                        try {
                            correctOrderList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    }else if (orderByPriceDescendingBox.isSelected()){
                        try {
                            correctOrderList=ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    } else if (!orderByPriceAscendingBox.isSelected() && !orderByPriceDescendingBox.isSelected()){
                        correctOrderList=productsFromCategoryList;
                    }
                    List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                    try {
                        resultList= ProductWithSizeAndQtity.getProductListBySize(Size.L,correctOrderList);
                    } catch (UnavailableException ex) {
                        ex.printStackTrace();
                    }
                    productModel.addAll(resultList);
                }else {
                    // do nothing
                }
            }
        });

        xlCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xlCheckBox.isSelected()){
                    List<ProductWithSizeAndQtity> productsFromCategoryList= new ArrayList<>();
                    productModel.removeAllElements();
                    if (allRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty()){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (accessoriesRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.ACCESSORIES)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (pantsRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.PANTS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (hoodieRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.HOODIE)){
                                productsFromCategoryList.add(product);
                            }
                        }

                    }else if (shirtRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SHIRT)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }else if (socksRadioButton.isSelected()){
                        for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                            if (!product.getSizesAndQuantitiesMap().isEmpty() && product.getProduct().getCategory().equals(Category.SOCKS)){
                                productsFromCategoryList.add(product);
                            }
                        }
                    }
                    List<ProductWithSizeAndQtity> correctOrderList= new ArrayList<>();
                    if (orderByPriceAscendingBox.isSelected()){
                        try {
                            correctOrderList= ProductWithSizeAndQtity.getProductListOrderedByPriceAsc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    }else if (orderByPriceDescendingBox.isSelected()){
                        try {
                            correctOrderList=ProductWithSizeAndQtity.getProductListOrderedByPriceDesc(productsFromCategoryList);
                        } catch (UnavailableException ex) {
                            ex.printStackTrace();
                        }

                    } else if (!orderByPriceAscendingBox.isSelected() && !orderByPriceDescendingBox.isSelected()){
                        correctOrderList=productsFromCategoryList;
                    }
                    List<ProductWithSizeAndQtity> resultList= new ArrayList<>();
                    try {
                        resultList= ProductWithSizeAndQtity.getProductListBySize(Size.XL,correctOrderList);
                    } catch (UnavailableException ex) {
                        ex.printStackTrace();
                    }
                    productModel.addAll(resultList);
                }else {
                    // do nothing
                }
            }
        });


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


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JTextField searchTextField= new JTextField();
        searchTextField.setMinimumSize(new Dimension(30,10));

        JButton searchButton= new JButton("Search");


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String typedText= searchTextField.getText();
                String noSpaces= typedText.replace(" ","");

                if (typedText.equals("")  || noSpaces.equals("")){
                    orderByPriceAscendingBox.setSelected(false);
                    orderByPriceDescendingBox.setSelected(false);
                    productModel.removeAllElements();
                    List<ProductWithSizeAndQtity> listTemp= new ArrayList<>();
                    for (ProductWithSizeAndQtity product : ProductWithSizeAndQtity.availableProductsWithSizesAndQtity){
                        if (!product.getSizesAndQuantitiesMap().isEmpty()){
                            listTemp.add(product);
                        }
                    }
                    productModel.addAll(listTemp);
                }else {
                    List<ProductWithSizeAndQtity> previousList = Collections.list(productModel.elements());
                    try {
                        List<ProductWithSizeAndQtity> listTemp= ProductWithSizeAndQtity.getProductListByName(typedText,previousList);
                        if (!listTemp.isEmpty()){
                            productModel.removeAllElements();
                            productModel.addAll(listTemp);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame,ex.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        JPanel searchPanel= new JPanel(new GridLayout(1,4,10,10));

        searchPanel.add(new JLabel("Type product name here: "));
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel());


        secondPanel.add(searchPanel,BorderLayout.NORTH);
        secondPanel.add(new JScrollPane(productList), BorderLayout.WEST);
        secondPanel.add(comboBoxPanel, BorderLayout.CENTER);
        secondPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.pack();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        int x = (screenWidth - windowWidth) / 2;
        int y = (screenHeight - windowHeight) / 2;

        frame.setLocation(x, y);
    }

    public static void ordersMenuClient(Customer customer, String loginEntered) {
        /**
         * This method changes frame to orders Menu for client
         * @param customer Customer value attached to entered login
         * @param loginEntered String login value entered while logging in
         */
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

        ordersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Order order = ordersList.getSelectedValue();
                productInfoTextArea.setText("ORDERED: \n" + order.getSelectedOrderInfo());
            }
        });

        JPanel listAndInfoPanel = new JPanel(new BorderLayout());
        listAndInfoPanel.add(new JScrollPane(ordersList), BorderLayout.CENTER);

        listAndInfoPanel.add(productInfoTextArea, BorderLayout.SOUTH);
        JButton backButton = backToMenuClient(customer, loginEntered);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);

        secondPanel.add(listAndInfoPanel, BorderLayout.CENTER);
        secondPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setTitle("Orders");
        frame.getContentPane().removeAll();
        frame.getContentPane().add(secondPanel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    public static Role login(String loginEntered, String passwordEntered) throws Exception {

        /**
         * This method generates hash for entered login and password
         * then compares it to the values stored in database
         * @param loginEntered String login value entered while logging in
         * @param passwordEntered String login value entered while logging in
         * @throws Exception when didnt find entered login or password in database
         * @return Role of the person who logged in
         */

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

        if (passwordToCheck.equals(combinedHashString)){
            return loginToCheck.getRole();
        }else {
            throw new Exception("Incorrect login or password");
        }
    }
    public static void readCustomersFromFile() throws Exception{
        /**
         * This method read all customers,their orders and cart from file
         * @throws Exception when file with specified path not found
         */
        try (BufferedReader br = new BufferedReader(new FileReader(customersFileName))) {
            String line;
            line=br.readLine();
            System.out.println("METODA READCUSTOMERSFROMFILE:");
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

                    while(scanner.hasNext()){
                        String oneProduct= scanner.next();
                        Scanner oneProductScanner= new Scanner(oneProduct);
                        oneProductScanner.useDelimiter(";");
                        String idString= oneProductScanner.next();
                        String idNoWhite =idString.trim();
                        int idProduct=Integer.parseInt(idNoWhite);
                        String category=oneProductScanner.next();
                        String nazwa= oneProductScanner.next();
                        String marka= oneProductScanner.next();
                        String koszt= oneProductScanner.next();
                        String opis= oneProductScanner.next();
                        Size size= Size.valueOf(oneProductScanner.next());
                        int quantity= oneProductScanner.nextInt();
                        cartList.add(new ProductInCartDTO(idProduct-1,size,quantity));
                    }
                }
                List<Integer> ordersIds= new ArrayList<>();

                if (!orders.equals("[]")){
                    String firstReplace= orders.replace("[","");
                    String secondReplace= firstReplace.replace("]","");

                    Scanner scanner= new Scanner(secondReplace);
                    scanner.useDelimiter(",");
                    while(scanner.hasNext()){
                        String idOrderString= scanner.next();
                        String trimmed=idOrderString.trim();
                        int idOrder= Integer.parseInt(trimmed);
                        ordersIds.add(idOrder);
                    }
                }
                Customer c= new Customer(loginHashed,firstName,lastName,address,telNumber,creditsDouble,email,cartList,ordersIds);
            }
        }
    }
    public static void readAccountsFromFile() throws Exception {
        /**
         * This method reads all account from file
         * @throws Exception when file with specified path not found
         */
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
                accounts.put(loginHashed,new PasswordRoleDTO(passwordHashedString,role));
            }
        }
    }
    public static void savePasswordChangesToFile(){
        /**
         * This method writes password changes to file
         */
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
        /**
         * This method writes to file all customers' changes
         */
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
        /**
         * This method changes frame to register
         */
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
        frame.getRootPane().setDefaultButton(registerButton);
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
        /**
         * This method registers new user
         * @param loginEntered String value of login entered while registering
         * @param passwordEntered String value of password entered while registering
         * @param confirmedPasswordEntered String value of confirmed password entered while registering
         * @throws Exception when login is already taken, password is less than 7 characters, entered passwords are not the same
         */
        int loginHashLong= loginEntered.hashCode();
        String loginHash= String.valueOf(loginHashLong);

        if (loginEntered.toCharArray().length<_minLoginLength){
            throw new Exception("Too short login! Minimum 5 characters!");
        }

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
    }
    public static void showAllAccounts(){
        /**
         * This method shows all acounts
         */
        for (Map.Entry<String, PasswordRoleDTO> entry : accounts.entrySet()) {
            String key = entry.getKey();
            PasswordRoleDTO passwordRoleDTO = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + passwordRoleDTO.getPassword() + ";" + passwordRoleDTO.getRole());
        }
    }
    public static void addCustomerToAccountsFile(Customer customer) throws Exception{
        /**
         * This method adds new registered user to file with accounts
         * @param customer Value of registered customer
         * @throws Exception when file with specified path not found
         */
        FileOutputStream fos = new FileOutputStream(accountsFileName, true);
        PrintStream ps = new PrintStream(fos);
        ps.println(customer.getLogin() + ";" + customer.getPassword() + ";" + _roleClientHash);
        ps.close();
        }
        public static JButton logOutButton(){
            /**
             * This method creates log out button
             * @return JButton with logging out function
             */
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
            /**
             * This method creates back to menu button
             * @param customer Customer value attached to entered login
             * @param login String value of entered login
             * @return JButton with going back to client's menu function
             */
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
        public static JButton backToMenuAdmin(){
        JButton backButton= new JButton("Back");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminLoggedIn();
            }
        });
        return backButton;
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
            /**
             * This method changes frame to add credits frame
             * @param customer Customer value attached to entered login
             * @param loginEntered String value of entered login
             */
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

            JTextField creditsTextField = new JTextField();
            Dimension textFieldSize = new Dimension((int)(Toolkit.getDefaultToolkit().getScreenResolution() * 0.78),
                    (int)(Toolkit.getDefaultToolkit().getScreenResolution() * 0.39));
            creditsTextField.setPreferredSize(textFieldSize);

            textFieldPanel.setLayout(new BoxLayout(textFieldPanel,BoxLayout.PAGE_AXIS));
            GridBagConstraints constraints= new GridBagConstraints();
            constraints.gridy=2;
            constraints.gridx=2;
            constraints.insets = new Insets(10, 10, 10, 10);

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

            frame.setTitle("Wallet");
            frame.getContentPane().removeAll();
            frame.getContentPane().add(secondPanel);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            frame.setVisible(true);
        }
        public static void accountScreenCustomer(Customer customer, String loginEntered){
            /**
             * This method changes frame to account customer screen
             * @param customer Customer value attached to entered login
             * @param loginEntered String value of entered login
             */
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
//                          password changing
                            char[] passwordChars = newPasswordField.getPassword();
                            String enteredPassword= new String(passwordChars);
                            String toHash=loginEntered+enteredPassword;
                            long newPasswordLong= toHash.hashCode();
                            String newPassword=String.valueOf(newPasswordLong);
                            customer.setPassword(newPassword);
                            long enteredLoginHash= loginEntered.hashCode();
                            String enteredLoginStringhash= String.valueOf(enteredLoginHash);
                            accounts.replace(enteredLoginStringhash,new PasswordRoleDTO(newPassword, Role.CLIENT));
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
            /**
             * This method changes frame to cart screen
             * @param customer Customer value attached to entered login
             * @param loginEntered String value of entered login
             */
            secondPanel = new JPanel();
            secondPanel.setLayout(new BorderLayout());

            frame.setTitle("Cart");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 300);
            JList<ProductInCartDTO> productsInCart= new JList<>();
            DefaultListModel<ProductInCartDTO> listModel= new DefaultListModel<>();
            JLabel label= new JLabel("Your cart: ");
            secondPanel.add(label,BorderLayout.NORTH);

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            double cost=calculateCartCost(customer.getCurrentCart());
            String formattedCredits = decimalFormat.format(cost);
            JLabel totalCostLabel= new JLabel("Cost: " + formattedCredits+"PLN");
            secondPanel.add(totalCostLabel,BorderLayout.EAST);

            for( ProductInCartDTO productInCartDTO : customer.getCurrentCart()){
                listModel.addElement(productInCartDTO);
            }
            productsInCart.setModel(listModel);

            secondPanel.add(new JScrollPane(productsInCart),BorderLayout.CENTER);

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
                    // verifying account data and payment
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

            frame.getContentPane().removeAll();
            frame.getContentPane().add(secondPanel);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            frame.setVisible(true);
        }
        public static double calculateCartCost(List<ProductInCartDTO> productInCartDTOS){
            /**
             * This method calculates cost of the current cart
             * @param productInCartDTOS List of products in cart
             * @return Cost of the current cart as a Double
             */
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
            /**
             * This method places an order
             * @param customer Customer value attached to entered login
             * @param loginEntered String value of entered login
             * @param productsInCart List of products in cart
             * @throws Exception When account data isn't filled, cart is empty, product's size or quantity is unavailable, there are not enough creduts to pay
             */
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

        for (ProductInCartDTO product : productsInCart){
            Optional<ProductWithSizeAndQtity> optionalProduct= ProductWithSizeAndQtity.availableProductsWithSizesAndQtity
                    .stream()
                    .filter(productWithSizeAndQtity -> productWithSizeAndQtity.getProduct().getId()-1==product.getIdProduct())
                    .findFirst();
            ProductWithSizeAndQtity p2Temp=null;
            if (optionalProduct.isPresent()){
                p2Temp=optionalProduct.get();
            }

            if (!ProductWithSizeAndQtity.availableProductsWithSizesAndQtity.contains(p2Temp)){
                throw new Exception("Product: " +  Product.allProducts.get(product.getIdProduct())+ "  is unavailable!");
            }
            // for each product in cart:
            // 1. get this product's id
            // 2. get its size and quantity
            // 3. create object ProductWithSizeAndQtity with id from 1.
            // 4. create map of available products using object from 3.
            // 5. if this map contains key SIZE then checks if the amount is available
            // 6. if all conditions are true-> add to LinkedHashMap object with key id from 1. and value DTO with size and quantity
            // 7. decreasing amount of products by sizes and quantities from cart
            // 8. placing new Order and clearing cart
            ProductWithSizeAndQtity productFromWarehouse=p2Temp;
            Size size= product.getSize();
            int quantity= product.getQuantity();
            LinkedHashMap<Size, Integer> sizesAndQuantitiesAvailable= productFromWarehouse.getSizesAndQuantitiesMap();

            if (sizesAndQuantitiesAvailable.containsKey(size)){
                int availableQuantity= sizesAndQuantitiesAvailable.get(size);
                if (quantity>availableQuantity){
                    throw new NotEnoughProductsException("For product: " + product.getProductName() + " this amount is unavailable at the moment!");
                }
                // increasing total cost by product cost * quantity
                totalCost+=ProductWithSizeAndQtity.availableProductsWithSizesAndQtity.get(Product.allProducts.get(product.getIdProduct()).getId()-1).getProduct().getPrice()*quantity;
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

            // decreasing credits by cost of the order
            customer.setCredits(customer.getCredits()-order.calculateCost());
            customer.getCurrentCart().clear();
            JOptionPane.showMessageDialog(frame,"Order nr " + order.getIdOrder()+ " has been placed!", "Order", JOptionPane.PLAIN_MESSAGE);

        }
        public static void registeredAnAccountScreen(){
            /**
             * This method changes frame to registered an account screen
             */
            secondPanel = new JPanel();
            secondPanel.setLayout(new BorderLayout());
            frame.setTitle("WELCOME!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            JLabel registeredLabel= new JLabel("Registered successfully!");

            registeredLabel.setFont(new Font(_FONT.getFontName(),Font.PLAIN,24));
            registeredLabel.setHorizontalAlignment(JLabel.CENTER);
            registeredLabel.setVerticalAlignment(JLabel.TOP);
            secondPanel.add(registeredLabel,BorderLayout.NORTH);

            JButton logInButton= new JButton("Continue");

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(logInButton);
            secondPanel.add(buttonPanel,BorderLayout.CENTER);
            logInButton.setBackground(new Color(160,255,125));

            logInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loggedOutScreen();
                }
            });

            frame.setTitle("Shop App");
            frame.getContentPane().removeAll();
            frame.getContentPane().add(secondPanel);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            frame.setVisible(true);
        }
}