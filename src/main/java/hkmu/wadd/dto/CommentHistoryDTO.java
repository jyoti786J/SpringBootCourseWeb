package hkmu.wadd.dto;

import java.time.LocalDateTime;

public class CommentHistoryDTO {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private String targetType; // "COURSE" or "POLL"
    private Long targetId;
    private String targetTitle;

    // Constructors, getters and setters
    public CommentHistoryDTO(Long commentId, String content, LocalDateTime createdAt,
                             String targetType, Long targetId, String targetTitle) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.targetType = targetType;
        this.targetId = targetId;
        this.targetTitle = targetTitle;
    }

    public Long getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }
}