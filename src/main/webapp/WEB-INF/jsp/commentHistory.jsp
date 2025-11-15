<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="comment.history.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="/view/${username}" class="button button-secondary">
        <spring:message code="comment.history.back.link"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1 class="history-header"><spring:message code="comment.history.title"/></h1>

        <c:choose>
            <c:when test="${empty comments}">
                <div class="empty-history-message">
                    <i class="fas fa-comment-slash"></i> <spring:message code="comment.history.empty.message"/>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${comments}" var="comment">
                    <div class="comment-item">
                        <div class="comment-header">
                            <div>
                                <span class="comment-type comment-type-${comment.targetType.toLowerCase()}">
                                    <spring:message code="comment.type.${comment.targetType.toLowerCase()}"/>
                                </span>
                                <span class="comment-target-link">
                                    <spring:message code="comment.history.on"/>
                                    <a href="/course/${comment.targetType eq 'POLL' ? 'poll/' :
                                      comment.targetType eq 'LECTURE' ? 'lecture/' : ''}${comment.targetId}">
                                            ${comment.targetTitle}
                                    </a>
                                </span>
                            </div>
                        </div>
                        <div class="comment-content">${comment.content}</div>
                        <div class="comment-date">
                            <i class="far fa-clock"></i> <spring:message code="comment.history.posted.on"/>
                            <fmt:formatDate value="${comment.createdAt}" pattern="MMM dd, yyyy hh:mm a"/>
                        </div>

                        <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                            <form action="/view/${username}/comments/${comment.commentId}/delete"
                                  method="post"
                                  class="mt-2">
                                <input type="hidden" name="targetType" value="${comment.targetType}"/>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit"
                                        class="button button-sm button-danger"
                                        onclick="return confirm('<spring:message code="comment.delete.confirm"/>')">
                                    <i class="fas fa-trash-alt"></i> <spring:message code="comment.delete.button"/>
                                </button>
                            </form>
                        </security:authorize>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>