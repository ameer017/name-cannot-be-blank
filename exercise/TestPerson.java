package exercise;

public class TestPerson {
  public static void printPerson(Person p) {
    System.out.println(p.toString());
    System.out.println();
  }

  public static void main(String[] args) {
    Person person = new Person("John Doe");
    printPerson(person);

    Employee employee = new Employee("Jane Smith", "1234567890", 100000);
    printPerson(employee);

    Faculty faculty = new Faculty(
        "Jim Beam", "1234567890", 100000, "8:00 AM - 5:00 PM",
        new Course[] {
            new Course("COSC212", "Introduction to Computer Science"),
            new Course("COSC201", "Introduction to Programming")});
    printPerson(faculty);
  }
}
