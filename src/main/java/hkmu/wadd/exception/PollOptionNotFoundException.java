package hkmu.wadd.exception;

public class PollOptionNotFoundException extends RuntimeException {
    public PollOptionNotFoundException(long optionId) {
            super("Poll Option" + optionId + " does not exist.");
    }
}

