package Models.Employees;

import Interfaces.IPersonInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractEmployee implements IPersonInfo {

    public static final String employeesFileName="src/Data/Employees.txt";
    public static int idCounter=1;
    int id;
    String firstName;
    String lastName;
    LocalDate hireDate;
    double salary;
    Role role;
    List<Feedback> feedbackFromManagerList = new ArrayList<>();


    public static List<AbstractEmployee> abstractEmployees = new ArrayList<>();
    public AbstractEmployee(){}


    public AbstractEmployee(String firstName, String lastName, LocalDate hireDate, double salary, Role role) {
        this.id=idCounter;
        idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireDate = hireDate;
        this.salary = salary;
        this.role = role;

    }

    @Override
    public String getPersonalData() {

        return this.id + ";"
                + this.firstName + ";"
                + this.lastName + ";"
                + this. hireDate + ";"
                + this.salary + ";"
                + this.role;
    }
    public static void showEmployees(){
        for (AbstractEmployee abstractEmployee : abstractEmployees){
            System.out.println(abstractEmployee.getPersonalData());
        }
    }
    public static void readEmployeesFromFile (){
        try{
            int employeesCounter=0;
            BufferedReader fileReader= new BufferedReader(new FileReader(employeesFileName));
            Scanner scanner= new Scanner(fileReader);


            while(scanner.hasNextLine()){


                String line= scanner.nextLine();
                Scanner lineScanner= new Scanner(line);
                lineScanner.useDelimiter(";");
                int id= lineScanner.nextInt();
                String firstName= lineScanner.next();
                String lastName= lineScanner.next();
                String hireDateString= lineScanner.next();
                LocalDate hireDate= LocalDate.parse(hireDateString);

                String salaryString=  lineScanner.next();
                double salary= Double.parseDouble(salaryString);
                String roleString= lineScanner.next();
                Role role= Role.valueOf(roleString);

                if (role==Role.MANAGER){
                    abstractEmployees.add(new Manager(firstName,lastName,hireDate,salary,role));
                }else if (role==Role.WORKER ){
                    abstractEmployees.add(new Worker(firstName,lastName,hireDate,salary,role));
                }else if(role==Role.ADMIN){
                    abstractEmployees.add(new Admin(firstName,lastName,hireDate,salary,role));
                }else if(role== Role.CONSULTANT){
                    abstractEmployees.add(new Consultant(firstName,lastName,hireDate,salary,role));
                }
            }
            idCounter+=employeesCounter;
            // nowo dodawani pracownicy beda mieli id ktorego jeszcze nie ma w pliku
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}