import java.util.Arrays;
import java.util.Comparator;

public class Student {

	public static final Comparator<Student> BY_NAME = new ByName();
	public static final Comparator<Student> BY_GRADE = new ByGrade();
	private String name;
	private int grade;

	// constructor here

	private static class ByName implements Comparator<Student> {

		public int compare(Student v, Student w) {
			return v.name.compareTo(w.name);
		}
	}

	private static class ByGrade implements Comparator<Student> {

		public int compare(Student v, Student w) {
			return v.grade - w.grade;
		}
	}

	public static void main(String[] args) {
		Student[] students = new Student[5];
		Arrays.sort(students, Student.BY_GRADE);
	}
}
