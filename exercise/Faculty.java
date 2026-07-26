package exercise;

import java.util.Arrays;

public class Faculty extends Employee {
  public String officeHours;
  public Course[] teachingCourses;

  public Faculty(String name, String ID, int payRate, String officeHours,
                 Course[] teachingCourses) {
    super(name, ID, payRate);
    this.officeHours = officeHours;
    this.teachingCourses = teachingCourses;
  }

  public String toString() {
    return super.toString() + " Office Hours: " + officeHours +
        " Teaching Courses: " + Arrays.toString(teachingCourses);
  }

  public String getOfficeHours() { return officeHours; }

  public void setOfficeHours(String officeHours) {
    this.officeHours = officeHours;
  }

  public Course[] getTeachingCourses() { return teachingCourses; }

  public void setTeachingCourses(Course[] teachingCourses) {
    this.teachingCourses = teachingCourses;
  }
}