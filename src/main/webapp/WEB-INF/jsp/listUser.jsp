<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="admin.user.management.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
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

<div class="container">
    <div class="header">
        <h1><spring:message code="admin.user.management.title"/></h1>
    </div>

    <div class="section">
        <div class="action-bar">
            <a href="/admin/users/create" class="button button-success">
                <i class="fas fa-plus"></i> <spring:message code="admin.user.add.button"/>
            </a>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success mb-3">
                <spring:message code="${success}"/>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger mb-3">
                <spring:message code="${error}"/>
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="admin-table">
                <thead>
                <tr>
                    <th><spring:message code="user.username.label"/></th>
                    <th><spring:message code="user.fullName.label"/></th>
                    <th><spring:message code="user.email.label"/></th>
                    <th><spring:message code="user.phone.label"/></th>
                    <th><spring:message code="user.role.label"/></th>
                    <th><spring:message code="user.actions.label"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.fullName}</td>
                        <td>${user.email}</td>
                        <td>${user.phone}</td>
                        <td><span class="role-badge role-${user.role.toLowerCase()}">
                                <spring:message code="role.${user.role}"/>
                            </span></td>
                        <td class="actions">
                            <a href="/admin/users/edit/${user.username}" class="button button-sm">
                                <i class="fas fa-edit"></i> <spring:message code="button.edit"/>
                            </a>
                            <form action="/admin/users/delete/${user.username}" method="post" class="inline-form">
                                <security:csrfInput />
                                <button type="submit" class="button button-sm button-danger"
                                        onclick="return confirm('<spring:message code="user.delete.confirm"/>')">
                                    <i class="fas fa-trash-alt"></i> <spring:message code="button.delete"/>
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="footer-actions">
            <a href="/course/index" class="button">
                <i class="fas fa-arrow-left"></i> <spring:message code="button.back.courses"/>
            </a>
        </div>
    </div>
</div>

</body>
</html>