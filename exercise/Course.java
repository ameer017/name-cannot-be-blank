package exercise;

public class Course {
  public String code;
  public String title;

  public Course(String code, String title) {
    this.code = code;
    this.title = title;
  }

  public String toString() {
    return code + " - " + title;
  }
}
