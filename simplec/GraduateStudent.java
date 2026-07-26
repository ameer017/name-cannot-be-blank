package simplec;

public class GraduateStudent extends Student {
  private String thesisTitle;

  public GraduateStudent(String studentName, String studentID, int age,
                         String department, float cgpa, String thesisTitle) {
    super(studentName, studentID, age, department, cgpa);
    this.thesisTitle = thesisTitle;
  }

  public void read() {
    System.out.println("I'm taking my breakfast now!");
    super.read();
  }

  public void makePresentation() {
    System.out.println("Graduate Student is making a presentation!");
  }

  @Override
  public String toString() {
    System.out.println("Accessing age: " + getAge());
    return super.toString() + "\nThesis Title: " + thesisTitle;
  }
}
