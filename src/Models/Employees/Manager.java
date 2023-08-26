package Models.Employees;

import Exceptions.InteractingWithYourselfException;

public class Manager extends Employee{
  public Manager(){
  }

  public void rateEmployee(Employee employee, Feedback feedback) throws InteractingWithYourselfException {
    if (this.id==employee.id){
      throw new InteractingWithYourselfException("You can't rate yourself!");
    }
    employee.feedbackFromManagerList.add(feedback);
    System.out.println("Employee has received your feedback.");
  }

  @Override
  public String getPersonalData() {
    return null;
  }
}
