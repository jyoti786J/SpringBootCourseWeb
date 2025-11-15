<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${poll.question}</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/course/index'/>" class="button button-secondary">
        <spring:message code="poll.back.link"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <div class="poll-header">
            <h1>${poll.question}</h1>
            <p class="poll-meta">
                <spring:message code="poll.total.votes" arguments="${poll.totalVotes}"/>
            </p>
        </div>

        <div class="poll-options">
            <form action="/course/${courseId}/poll/${poll.id}/vote" method="post" class="poll-form">
                <c:forEach items="${poll.options}" var="option">
                    <div class="option-item">
                        <label class="option-label">
                            <input type="radio" name="optionId" value="${option.id}"
                                   <c:if test="${userVote != null && userVote.selectedOption.id == option.id}">checked</c:if>>
                            <span class="option-text">${option.text}</span>
                            <span class="vote-count">
                                (<spring:message code="poll.vote.count" arguments="${option.voteCount}"/>)
                            </span>
                        </label>
                    </div>
                </c:forEach>
                <div class="form-actions">
                    <button type="submit" class="button button-primary">
                        <i class="fas fa-vote-yea"></i> <spring:message code="poll.vote.button"/>
                    </button>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>

        <div class="comments-section mt-5">
            <h3><spring:message code="poll.comments.title"/></h3>

            <security:authorize access="isAuthenticated()">
                <form action="/course/${courseId}/poll/${poll.id}/comment" method="post" class="comment-form">
                    <textarea name="content" rows="3"
                              placeholder="<spring:message code='poll.comment.placeholder'/>" required></textarea>
                    <div class="form-actions">
                        <button type="submit" class="button button-primary">
                            <i class="fas fa-comment"></i> <spring:message code="poll.comment.button"/>
                        </button>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </security:authorize>

            <div class="comments-list">
                <c:choose>
                    <c:when test="${not empty poll.comments && !poll.comments.isEmpty()}">
                        <c:forEach items="${poll.comments}" var="comment">
                            <div class="comment-item">
                                <div class="comment-header">
                                    <div class="comment-author">
                                        <i class="fas fa-user"></i> ${comment.author.username}
                                    </div>
                                    <div class="comment-date">
                                        <i class="fas fa-clock"></i>
                                        <fmt:formatDate value="${comment.createdAt}" pattern="MMM dd, yyyy hh:mm a"/>
                                    </div>
                                </div>
                                <div class="comment-content">
                                        ${comment.content}
                                </div>
                                <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                                    <form action="/course/${courseId}/poll/${poll.id}/comment/${comment.id}/delete"
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
                            <i class="fas fa-info-circle"></i> <spring:message code="poll.no.comments"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</body>
</html>