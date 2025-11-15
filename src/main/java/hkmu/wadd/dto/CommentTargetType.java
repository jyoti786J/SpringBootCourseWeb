package hkmu.wadd.dto;

public enum CommentTargetType {
    LECTURE, COURSE, POLL;

    // Add this method for case-insensitive conversion
    public static CommentTargetType fromString(String value) {
        try {
            return CommentTargetType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown comment target type: " + value);
        }
    }
}