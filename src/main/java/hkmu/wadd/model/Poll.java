package hkmu.wadd.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "poll")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PollOption> options = new LinkedHashSet<>(); // Changed to Set

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private Set<PollComment> comments = new LinkedHashSet<>(); // Changed to Set
    @Transient
    public int getTotalVotes() {
        if (options == null || options.isEmpty()) {
            return 0;
        }
        return options.stream()
                .mapToInt(PollOption::getVoteCount)
                .sum();
    }

    // Constructors
    public Poll() {
    }

    public Poll(String question, Course course, String createdBy) {
        this.question = question;
        this.course = course;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Set<PollOption> getOptions() {
        return options;
    }

    public void setOptions(Set<PollOption> options) {
        this.options = options;
    }

    public Set<PollComment> getComments() {
        return comments;
    }

    public void setComments(Set<PollComment> comments) {
        this.comments = comments;
    }

    // Helper methods
    public void addOption(PollOption option) {
        options.add(option);
        option.setPoll(this);
    }

    public void removeOption(PollOption option) {
        options.remove(option);
        option.setPoll(null);
    }

    public void addComment(PollComment comment) {
        comments.add(comment);
        comment.setPoll(this);
    }

    public void removeComment(PollComment comment) {
        comments.remove(comment);
        comment.setPoll(null);
    }
}