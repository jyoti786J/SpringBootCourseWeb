package hkmu.wadd.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "lecture_no")
    private int lectureNo;

    @Column(name = "lecture_name")
    private String lectureName;
    // Other lecture attributes

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<LectureNotes> lectureNotes = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLectureNo() {
        return lectureNo;
    }

    public void setLectureNo(int lectureNo){
        this.lectureNo= lectureNo;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<LectureNotes> getLectureNotes() {
        return lectureNotes;
    }

    public void setLectureNotes(List<LectureNotes> lectureNotes) {
        this.lectureNotes = lectureNotes;
    }

    public void deleteLectureNotes(LectureNotes note) {
        this.lectureNotes.remove(note);
        note.setLecture(null);
    }

    public void addLectureNote(LectureNotes note) {
        note.setLecture(this);
        this.lectureNotes.add(note);
    }

}