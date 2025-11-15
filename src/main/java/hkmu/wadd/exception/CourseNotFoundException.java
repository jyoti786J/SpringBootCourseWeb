package hkmu.wadd.exception;



public class CourseNotFoundException extends Exception {
    public CourseNotFoundException() {
        super("No course found");
    }

    public CourseNotFoundException(long id) {
        super("Course" + id + " does not exist.");
    }
}