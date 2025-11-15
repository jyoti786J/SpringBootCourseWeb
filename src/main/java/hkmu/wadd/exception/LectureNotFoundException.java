package hkmu.wadd.exception;

public class LectureNotFoundException extends Exception {
    public LectureNotFoundException(long id) {
        super("Lecture " + id + " does not exist.");
    }
}