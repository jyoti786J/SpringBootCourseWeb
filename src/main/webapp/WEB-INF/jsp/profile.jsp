<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="profile.header" arguments="${user.fullName}"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <security:authorize access="isAuthenticated()">
        <c:set var="username" value="${pageContext.request.userPrincipal.name}"/>
        <!-- Edit Profile Button (visible to profile owner) -->
        <!-- Logout Button -->
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <button type="submit" class="button button-danger"><spring:message code="logout"/></button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </security:authorize>
</div>

<div class="container">
    <div class="section">
        <div class="profile-header text-center">
            <h1><spring:message code="profile.header" arguments="${user.fullName}"/></h1>
            <div class="role-badge role-${user.role.toLowerCase()}">
                <spring:message code="role.${user.role}"/>
            </div>
        </div>

        <div class="profile-details">
            <div class="detail-item">
                <span class="detail-label">
                    <i class="fas fa-user"></i> <spring:message code="profile.username.label"/>
                </span>
                ${user.username}
            </div>

            <div class="detail-item">
                <span class="detail-label">
                    <i class="fas fa-id-card"></i> <spring:message code="profile.fullName.label"/>
                </span>
                ${user.fullName}
            </div>

            <div class="detail-item">
                <span class="detail-label">
                    <i class="fas fa-envelope"></i> <spring:message code="profile.email.label"/>
                </span>
                ${user.email}
            </div>

            <div class="detail-item">
                <span class="detail-label">
                    <i class="fas fa-phone"></i> <spring:message code="profile.phone.label"/>
                </span>
                ${user.phone}
            </div>
        </div>

        <div class="action-links">
            <!-- Edit Profile Button (alternative position) -->
            <security:authorize access="isAuthenticated()">
                <c:set var="username" value="${pageContext.request.userPrincipal.name}"/>
                <c:if test="${user.username eq username}">
                    <a href="<c:url value='/view/${user.username}/edit'/>" class="button">
                        <i class="fas fa-edit"></i> <spring:message code="profile.edit.button"/>
                    </a>
                </c:if>
            </security:authorize>

            <a href="<c:url value='/view/${user.username}/votes'/>" class="button button-secondary">
                <i class="fas fa-vote-yea"></i> <spring:message code="profile.viewVotes.button"/>
            </a>
            <a href="<c:url value='/view/${user.username}/comments'/>" class="button button-secondary">
                <i class="fas fa-comments"></i> <spring:message code="profile.viewComments.button"/>
            </a>
            <a href="<c:url value='/course/index'/>" class="button">
                <i class="fas fa-arrow-left"></i> <spring:message code="profile.back.link"/>
            </a>
        </div>
    </div>
</div>
</body>
</html>