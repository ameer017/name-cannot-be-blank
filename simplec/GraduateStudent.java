package simplec;

public class GraduateStudent extends Student {
  private String thesisTitle;

  public GraduateStudent(String studentName, String studentID, int age,
                         String department, float cgpa, String thesisTitle) {
    super(studentName, studentID, age, department, cgpa);
    this.thesisTitle = thesisTitle;
  }

  @Override
  public String toString() {
    return super.toString() + "\nThesis Title: " + thesisTitle;
  }


}