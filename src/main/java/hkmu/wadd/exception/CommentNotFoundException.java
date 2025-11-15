package hkmu.wadd.exception;

public class CommentNotFoundException extends Exception {
    public CommentNotFoundException(Long commentId) {
        super("Comment with id " + commentId + " not found");
    }
}
