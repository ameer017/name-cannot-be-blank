package simplec;

public class TestStudent {
  public static void main(String[] args) {
    Student st1 =
        new Student("Mariam", "U23DLCS1001", 18, "Computer Science", 3.9f);

    Student st2 =
        new Student("John", "U23DLCS1002", 29, "Information Technology", 3.7f);

    Student st3 = new Student("Jane", "U23DLCS1003", "Computer Science");

    System.out.println(st1.toString());
    System.out.println();
    st1.setStudentName("Qawiyyah");
    System.out.println("Updated Student Name:");
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

    GraduateStudent gs1 = new GraduateStudent("Ali", "U23DLCS1004", 25, "Computer Science", 4.99f, "Blockchain Technology");
    System.out.println(gs1.toString());
    gs1.read();
  }
}