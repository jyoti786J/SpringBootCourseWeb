<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="lecture.header" arguments="${lecture.lectureNo},${lecture.lectureName}"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/course/index'/>" class="button button-secondary">
        <spring:message code="lecture.back.link"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <div class="lecture-header">
            <h1><spring:message code="lecture.header" arguments="${lecture.lectureNo},${lecture.lectureName}"/></h1>
        </div>

        <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
            <div class="action-buttons mb-4">
                <a href="/course/${courseId}/lecture/${lecture.id}/edit" class="button">
                    <i class="fas fa-edit"></i> <spring:message code="lecture.edit.button"/>
                </a>
                <form action="/course/${courseId}/lecture/${lecture.id}/delete" method="post" class="inline-form">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="button button-danger"
                            onclick="return confirm('<spring:message code="lecture.delete.confirm"/>')">
                        <i class="fas fa-trash-alt"></i> <spring:message code="lecture.delete.button"/>
                    </button>
                </form>
            </div>
        </security:authorize>

        <div class="lecture-notes-section">
            <div class="section-header">
                <h3><spring:message code="lecture.notes.title"/></h3>
                <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                    <a href="/course/${courseId}/lecture/${lecture.id}/note/add" class="button button-success">
                        <i class="fas fa-plus"></i> <spring:message code="lecture.note.add.button"/>
                    </a>
                </security:authorize>
            </div>

            <c:choose>
                <c:when test="${not empty notes && !notes.isEmpty()}">
                    <div class="notes-grid">
                        <c:forEach items="${notes}" var="note">
                            <div class="note-item">
                                <div class="note-header">
                                    <h4>${note.name}</h4>
                                    <div class="note-meta">
                                        <span class="note-uploader">
                                            <i class="fas fa-user"></i> ${note.uploadedBy}
                                        </span>
                                        <span class="note-date">
                                            <i class="fas fa-calendar-alt"></i>
                                            <fmt:formatDate value="${note.uploadedAt}" pattern="MMM dd, yyyy"/>
                                        </span>
                                    </div>
                                </div>
                                <div class="note-actions">
                                    <a href="/course/${courseId}/lecture/${lecture.id}/note/${note.id}"
                                       class="button button-sm" download>
                                        <i class="fas fa-download"></i> <spring:message code="lecture.note.download"/>
                                    </a>
                                    <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                                        <form action="/course/${courseId}/lecture/${lecture.id}/note/${note.id}/delete"
                                              method="post" class="inline-form">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button type="submit" class="button button-sm button-danger"
                                                    onclick="return confirm('<spring:message code="lecture.note.delete.confirm"/>')">
                                                <i class="fas fa-trash-alt"></i> <spring:message code="lecture.note.delete.button"/>
                                            </button>
                                        </form>
                                    </security:authorize>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-message">
                        <i class="fas fa-info-circle"></i> <spring:message code="lecture.notes.empty"/>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="comments-section mt-5">
            <h3><spring:message code="lecture.comments.title"/></h3>

            <security:authorize access="isAuthenticated()">
                <form:form method="POST" modelAttribute="commentForm"
                           action="/course/${courseId}/lecture/${lecture.id}/comment" class="comment-form">
                    <textarea name="content" rows="3" placeholder="<spring:message code='comment.placeholder'/>" required></textarea>
                    <div class="form-actions">
                        <button type="submit" class="button button-primary">
                            <i class="fas fa-comment"></i> <spring:message code="lecture.comment.submit"/>
                        </button>
                    </div>
                </form:form>
            </security:authorize>

            <div class="comments-list">
                <c:choose>
                    <c:when test="${not empty comments && !comments.isEmpty()}">
                        <c:forEach items="${comments}" var="comment">
                            <div class="comment-item">
                                <div class="comment-header">
                                    <div class="comment-author">
                                        <i class="fas fa-user"></i> ${comment.author.username}
                                    </div>
                                    <div class="comment-date">
                                        <i class="fas fa-clock"></i>
                                        <fmt:formatDate value="${comment.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                    </div>
                                </div>
                                <div class="comment-content">
                                        ${comment.content}
                                </div>
                                <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                                    <form action="/course/${courseId}/lecture/${lecture.id}/comment/${comment.id}/delete"
                                          method="post" class="comment-actions">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="button button-sm button-danger"
                                                onclick="return confirm('<spring:message code="comment.delete.confirm"/>')">
                                            <i class="fas fa-trash-alt"></i> <spring:message code="comment.delete.button"/>
                                        </button>
                                    </form>
                                </security:authorize>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-message">
                            <i class="fas fa-info-circle"></i> <spring:message code="lecture.comments.empty"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</body>
</html>