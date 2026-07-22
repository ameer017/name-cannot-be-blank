package simplec;

public class Student {
  public String studentName;
  public String studentID;
  public int age;
  public String department;
  public float cgpa;

  public Student(String studentName, String studentID, int age,
                 String department, float cgpa) {
    this.studentName = studentName;
    this.studentID = studentID;
    this.age = age;
    this.department = department;
    this.cgpa = cgpa;
  }

  public Student(String name, String studentID, String department) {
    this(name, studentID, 20, department, 0.0f);
  }

  public void write() {
    System.out.println(studentName + " is to write anything!");
  }
  public void read() {
    System.out.println(studentName + " is to read anything!");
  }

  public void learn(String course) {
    System.out.println(studentName + " is learning " + course);
  }
  public void takeQuiz() {
    System.out.println("Welcome " + studentName);
    System.out.println("Choose the course to the Quiz:");
    System.out.println("1. COSC212");
    System.out.println("2. COSC201");
    System.out.println("3. COSC203");
  }
  public String toString() {
    return "Student Name: " + studentName + "\nStudent ID: " + studentID +
        "\nDepartment: " + department + "\nAge: " + age + "\nCGPA: " + cgpa;
  }
  public static void main(String[] args) {
    Student st1 =
        new Student("Mariam", "U23DLCS1001", 18, "Computer Science", 3.9f);

    Student st2 =
        new Student("John", "U23DLCS1002", 29, "Information Technology", 3.7f);

    Student st3 = new Student("Jane", "U23DLCS1003", "Computer Science");

    System.out.println(st1.toString());
    System.out.println();
    System.out.println(st2.toString());
    System.out.println();
    System.out.println(st3.toString());
    st1.read();
    st2.write();
    st2.takeQuiz();
    st1.learn("COSC212");
    System.out.println("Age: " + 34 + "yrs");
    System.out.println("CGPA: " + 3.8f);
  }
}