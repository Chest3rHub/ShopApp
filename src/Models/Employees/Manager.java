package Models.Employees;

import Exceptions.InteractingWithYourselfException;

import java.time.LocalDate;

public class Manager extends AbstractEmployee {

  public Manager(String firstName, String lastName, LocalDate hireDate, double salary, Role role){
    super(firstName,lastName,hireDate,salary,role);
  }

  public void rateEmployee(AbstractEmployee abstractEmployee, Feedback feedback) throws InteractingWithYourselfException {
    if (this.id== abstractEmployee.id){
      throw new InteractingWithYourselfException("You can't rate yourself!");
    }
    abstractEmployee.feedbackFromManagerList.add(feedback);
    System.out.println("Employee has received your feedback.");
  }
  public void hireNewEmployee(AbstractEmployee abstractEmployee){
    if (AbstractEmployee.abstractEmployees.contains(abstractEmployee)){
      System.out.println("This employee is already hired!");
      return;
    }
    AbstractEmployee.abstractEmployees.add(abstractEmployee);
    System.out.println("Hired new employee: " + abstractEmployee);

  }
  public void fireEmployee(AbstractEmployee abstractEmployee){
    if (!AbstractEmployee.abstractEmployees.contains(abstractEmployee)){
      System.out.println("There is no such employee to fire.");
      return;
    }
    AbstractEmployee.abstractEmployees.remove(abstractEmployee);
    System.out.println("Fired an employee: " + abstractEmployee);
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
