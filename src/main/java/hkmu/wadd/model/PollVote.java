package hkmu.wadd.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PollVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PollOption selectedOption;


    @JoinColumn(name = "username", referencedColumnName = "username")
    private String username;

    @ManyToOne
    private Poll poll;

    @Column(nullable = false, updatable = false)
    private LocalDateTime votedAt = LocalDateTime.now();

    public LocalDateTime getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(LocalDateTime votedAt) {
        this.votedAt = votedAt;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PollOption getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(PollOption selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
}