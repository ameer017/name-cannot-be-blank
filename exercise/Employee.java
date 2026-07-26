package exercise;

public class Employee extends Person {
  public String ID;
  public int payRate;

  public Employee(String name, String ID, int payRate) {
    super(name);
    this.ID = ID;
    this.payRate = payRate;
  }

  public int getPay() { return payRate; }

  @Override
  public String toString() {
    return "Employee name: " + name + " ID: " + ID + " Pay Rate: " + payRate;
  }
}