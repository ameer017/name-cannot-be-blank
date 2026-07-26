package simplec;

public class Student {
  private String studentName;
  private String studentID;
  private int age;
  private String department;
  private float cgpa;

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

  // ================getters/accessors=================
  public String getStudentName() { return studentName; }

  public int getAge() { return age; }

  // ================setters/mutators=================
  public void setStudentName(String name) { studentName = name; }

  // ================other methods=================
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
}