package Models.Employees;

import Exceptions.InteractingWithYourselfException;

import java.time.LocalDate;

public class Manager extends AbstractEmployee {

  public Manager(String firstName, String lastName, LocalDate hireDate, double salary, Role role){
    super(firstName,lastName,hireDate,salary,role);
  }

  public void rateEmployee(Consultant consultant, Feedback feedback) throws InteractingWithYourselfException {
    if (this.id== consultant.id){
      throw new InteractingWithYourselfException("You can't rate yourself!");
    }
    consultant.feedbackFromManagerList.add(feedback);
    System.out.println("Employee has received your feedback.");
  }
  public void rateEmployee(Worker worker, Feedback feedback) throws InteractingWithYourselfException {
    if (this.id== worker.id){
      throw new InteractingWithYourselfException("You can't rate yourself!");
    }
    worker.feedbackFromManagerList.add(feedback);
    System.out.println("Employee has received your feedback.");
  }
  public void hireNewEmployee(Worker worker){
    if (AbstractEmployee.abstractEmployees.contains(worker)){
      System.out.println("This employee is already hired!");
      return;
    }
    AbstractEmployee.abstractEmployees.add(worker);
    System.out.println("Hired new employee: " + worker);

  }
  public void hireNewEmployee(Consultant consultant){
    if (AbstractEmployee.abstractEmployees.contains(consultant)){
      System.out.println("This employee is already hired!");
      return;
    }
    AbstractEmployee.abstractEmployees.add(consultant);
    System.out.println("Hired new employee: " + consultant);

  }
  public void fireEmployee(Consultant consultant){
    if (!AbstractEmployee.abstractEmployees.contains(consultant)){
      System.out.println("There is no such employee to fire.");
      return;
    }
    AbstractEmployee.abstractEmployees.remove(consultant);
    System.out.println("Fired an employee: " + consultant);
  }
  public void fireEmployee(Worker worker){
    if (!AbstractEmployee.abstractEmployees.contains(worker)){
      System.out.println("There is no such employee to fire.");
      return;
    }
    AbstractEmployee.abstractEmployees.remove(worker);
    System.out.println("Fired an employee: " + worker);
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
}
