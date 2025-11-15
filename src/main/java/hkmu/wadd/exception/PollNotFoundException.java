package hkmu.wadd.exception;

public class PollNotFoundException extends Exception {
    public PollNotFoundException(long id) {
        super("Poll " + id + " does not exist.");
    }
}
