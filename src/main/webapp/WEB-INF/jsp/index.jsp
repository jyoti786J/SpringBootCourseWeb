<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Programming Course</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <security:authorize access="!isAuthenticated()">
        <a href="<c:url value='/login'/>" class="button button-primary"><spring:message code="login"/></a>
        <a href="<c:url value='/register'/>" class="button"><spring:message code="register"/></a>
    </security:authorize>
    <security:authorize access="isAuthenticated()">
        <c:set var="username" value="${pageContext.request.userPrincipal.name}"/>
        <a href="<c:url value='/view/${username}'/>" class="button"><spring:message code="profile"/></a>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <button type="submit" class="button button-danger"><spring:message code="logout"/></button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </security:authorize>
</div>

<div class="header">
    <h1>
        <c:choose>
            <c:when test="${not empty course}">
                ${course.courseName}
            </c:when>
            <c:otherwise>
                <spring:message code="system.name"/>
            </c:otherwise>
        </c:choose>
    </h1>
</div>

<security:authorize access="!isAuthenticated()">
    <div class="section text-center">
        <h2><spring:message code="welcome.title"/></h2>
        <p class="mb-3"><spring:message code="welcome.message"/></p>
        <a href="<c:url value='/register'/>" class="button button-primary"><spring:message code="welcome.join"/></a>
    </div>
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
    <div class="section">
        <h3><spring:message code="admin.section"/></h3>
        <a href="<c:url value='/admin/users'/>" class="button"><spring:message code="admin.manage_users"/></a>
    </div>
</security:authorize>

<div class="section">
    <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
        <a href="/course/${course.id}/lecture/add" class="button button-success mb-3">
            <spring:message code="lecture.create_new"/>
        </a>
    </security:authorize>

    <h2><spring:message code="course.lectures"/></h2>
    <c:choose>
        <c:when test="${not empty lectures && !lectures.isEmpty()}">
            <c:forEach items="${lectures}" var="lecture">
                <div class="lecture-item">
                    <h3>
                        <a href="/course/${course.id}/lecture/${lecture.id}" class="lecture-link">
                            <spring:message code="lecture.title" arguments="${lecture.lectureNo},${lecture.lectureName}"/>
                        </a>
                        <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                            <span class="lecture-actions">
                                <a href="/course/${course.id}/lecture/${lecture.id}/edit" class="button">
                                    <spring:message code="edit"/>
                                </a>
                                <form action="/course/${course.id}/lecture/${lecture.id}/delete" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="button button-danger">
                                        <spring:message code="delete"/>
                                    </button>
                                </form>
                            </span>
                        </security:authorize>
                    </h3>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p><spring:message code="lecture.none_available"/></p>
        </c:otherwise>
    </c:choose>
</div>

<div class="section">
    <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
        <a href="/course/${course.id}/poll/create" class="button button-success mb-3">
            <spring:message code="poll.create_new"/>
        </a>
    </security:authorize>

    <h2><spring:message code="course.polls"/></h2>
    <c:choose>
        <c:when test="${not empty polls && !polls.isEmpty()}">
            <c:forEach items="${polls}" var="poll">
                <div class="poll-item">
                    <h3>
                        <a href="/course/${course.id}/poll/${poll.id}" class="poll-link">
                                ${poll.question}
                        </a>
                        <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                            <span class="poll-actions">
                                <a href="/course/${course.id}/poll/${poll.id}/edit" class="button">
                                    <spring:message code="edit"/>
                                </a>
                                <form action="/course/${course.id}/poll/${poll.id}/delete" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="button button-danger">
                                        <spring:message code="delete"/>
                                    </button>
                                </form>
                            </span>
                        </security:authorize>
                    </h3>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p><spring:message code="poll.none_available"/></p>
        </c:otherwise>
    </c:choose>
</div>

<div class="section">
    <h2><spring:message code="course.comments"/></h2>
    <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
        <form action="/course/comment" method="post" class="mb-3">
            <textarea name="content" rows="3" placeholder="<spring:message code='comment.placeholder'/>" required></textarea>
            <button type="submit" class="button button-primary">
                <spring:message code="comment.post"/>
            </button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </security:authorize>

    <div class="comments-container">
        <c:choose>
            <c:when test="${not empty course.comments && !course.comments.isEmpty()}">
                <c:forEach items="${course.comments}" var="comment">
                    <div class="comment-item mb-3">
                        <div class="comment-header">
                            <strong>${comment.author.username}</strong>
                            <small>
                                <fmt:formatDate value="${comment.createdAt}" pattern="MMM dd, yyyy hh:mm a"/>
                            </small>
                        </div>
                        <p class="comment-content">${comment.content}</p>
                        <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
                            <form action="/course/${course.id}/comment/${comment.id}/delete"
                                  method="post"
                                  class="mt-2">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <button type="submit"
                                        class="button button-danger"
                                        onclick="return confirm('<spring:message code="comment.delete.confirm"/>')">
                                    <spring:message code="comment.delete.button"/>
                                </button>
                            </form>
                        </security:authorize>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p><spring:message code="comment.none_available"/></p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>
