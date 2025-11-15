<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="user.add.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
    <style>
        .error-message {
            color: #dc3545;
            font-size: 0.875em;
            margin-top: 0.25rem;
        }
        .is-invalid {
            border-color: #dc3545;
        }
    </style>
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
    <div class="section">
        <h1 class="text-center"><spring:message code="user.add.title"/></h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger mb-3">
                <spring:message code="${error}"/>
            </div>
        </c:if>

        <form method="POST" action="/admin/users/create" class="user-form">
            <security:csrfInput />

            <div class="form-group">
                <label><spring:message code="user.username.label"/></label>
                <input type="text" name="username" required
                       class="form-control ${not empty errors.username ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='user.username.placeholder'/>">
                <c:if test="${not empty errors.username}">
                    <div class="error-message">
                        <spring:message code="${errors.username}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label><spring:message code="user.password.label"/></label>
                <input type="password" name="password" required
                       class="form-control ${not empty errors.password ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='user.password.placeholder'/>">
                <c:if test="${not empty errors.password}">
                    <div class="error-message">
                        <spring:message code="${errors.password}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label><spring:message code="user.confirmPassword.label"/></label>
                <input type="password" name="confirmPassword" required
                       class="form-control ${not empty errors.confirmPassword ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='user.confirmPassword.placeholder'/>">
                <c:if test="${not empty errors.confirmPassword}">
                    <div class="error-message">
                        <spring:message code="${errors.confirmPassword}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label><spring:message code="user.fullName.label"/></label>
                <input type="text" name="fullName" required
                       class="form-control ${not empty errors.fullName ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='user.fullName.placeholder'/>">
                <c:if test="${not empty errors.fullName}">
                    <div class="error-message">
                        <spring:message code="${errors.fullName}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label><spring:message code="user.email.label"/></label>
                <input type="email" name="email" required
                       class="form-control ${not empty errors.email ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='user.email.placeholder'/>">
                <c:if test="${not empty errors.email}">
                    <div class="error-message">
                        <spring:message code="${errors.email}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label><spring:message code="user.phone.label"/></label>
                <input type="text" name="phone" required
                       class="form-control ${not empty errors.phone ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='user.phone.placeholder'/>">
                <c:if test="${not empty errors.phone}">
                    <div class="error-message">
                        <spring:message code="${errors.phone}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label><spring:message code="user.role.label"/></label>
                <select name="role" required class="form-control ${not empty errors.role ? 'is-invalid' : ''}">
                    <c:forEach items="${availableRoles}" var="role">
                        <option value="${role}"><spring:message code="role.${role}"/></option>
                    </c:forEach>
                </select>
                <c:if test="${not empty errors.role}">
                    <div class="error-message">
                        <spring:message code="${errors.role}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-user-plus"></i> <spring:message code="user.create.button"/>
                </button>
                <a href="/admin/users" class="button">
                    <i class="fas fa-arrow-left"></i> <spring:message code="user.back.link"/>
                </a>
            </div>
        </form>
    </div>
</div>
</body>
</html>