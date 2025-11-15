package hkmu.wadd.exception;

import java.util.UUID;

public class LectureNoteNotFoundException extends Exception {
    public LectureNoteNotFoundException(UUID id) {
        super("Lecture note " + id + " does not exist.");
    }
}