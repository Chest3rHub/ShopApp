package Models.Employees;

import Interfaces.IPersonInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
    List<Feedback> feedbackFromManagerList;


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
        abstractEmployees.add(this);

    }

    public String getFirstName() {
        return firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Feedback> getFeedbackFromManagerList() {
        return feedbackFromManagerList;
    }

    public void setFeedbackFromManagerList(List<Feedback> feedbackFromManagerList) {
        this.feedbackFromManagerList = feedbackFromManagerList;
    }

    public static List<AbstractEmployee> getAbstractEmployees() {
        return abstractEmployees;
    }

    public static void setAbstractEmployees(List<AbstractEmployee> abstractEmployees) {
        AbstractEmployee.abstractEmployees = abstractEmployees;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
    public String toString(){
        return "ID: "+ this.id + ", NAME: "
                + this.firstName + ", SURNAME: "
                + this.lastName + ", ROLE: "
                + this.role;
    }
    public static void showEmployees(){
        for (AbstractEmployee abstractEmployee : abstractEmployees){
            System.out.println(abstractEmployee.getPersonalData());
        }
    }
    public static void readEmployeesFromFile (){
        /**
         * This method reads employees from file and adds them to static collection abstractEmployees
         */
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
                    new Manager(firstName,lastName,hireDate,salary,role);
                }else if (role==Role.WORKER ){
                    new Worker(firstName,lastName,hireDate,salary,role);
                }else if(role==Role.ADMIN){
                    new Admin(firstName,lastName,hireDate,salary,role);
                }else if(role== Role.CONSULTANT){
                    new Consultant(firstName,lastName,hireDate,salary,role);
                }
            }
            idCounter+=employeesCounter;
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void saveEmployeesChangesToFile(){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(employeesFileName));
            for (AbstractEmployee employee : abstractEmployees) {
                    bufferedWriter.write(employee.getId() + ";"
                            + employee.getFirstName() + ";"
                            + employee.getLastName() + ";"
                            + employee.getHireDate() + ";"
                            + employee.getSalary() + ";"
                    + employee.getRole());
                    bufferedWriter.write("\n");

            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}