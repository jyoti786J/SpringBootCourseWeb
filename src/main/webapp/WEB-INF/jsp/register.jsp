<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="register.header"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='course/index'/>" class="button"><spring:message code="view.course"/></a>
</div>

<div class="register-container">
    <div class="section">
        <h1 class="text-center"><spring:message code="register.header"/></h1>

        <c:if test="${not empty error}">
            <div class="error-message mb-3"><spring:message code="${error}"/></div>
        </c:if>

        <form method="POST" modelAttribute="user" class="register-form">
            <security:csrfInput />

            <div class="form-group">
                <label><spring:message code="register.username"/></label>
                <input type="text" name="username" required
                       placeholder="<spring:message code='register.username.placeholder'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="register.password"/></label>
                <input type="password" name="password" required
                       placeholder="<spring:message code='register.password.placeholder'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="register.confirmPassword"/></label>
                <input type="password" name="confirmPassword" required
                       placeholder="<spring:message code='register.confirmPassword.placeholder'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="register.fullName"/></label>
                <input type="text" name="fullName" required
                       placeholder="<spring:message code='register.fullName.placeholder'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="register.email"/></label>
                <input type="email" name="email" required
                       placeholder="<spring:message code='register.email.placeholder'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="register.phone"/></label>
                <input type="text" name="phone" required
                       placeholder="<spring:message code='register.phone.placeholder'/>">
            </div>

            <div class="form-group" style="display: none;">
                <label for="role"><spring:message code="register.role"/></label>
                <input type="text" name="role" id="role" value="STUDENT" disabled>
            </div>

            <button type="submit" class="button button-primary"><spring:message code="register.button"/></button>
        </form>

        <div class="login-link mt-3 text-center">
            <spring:message code="register.login.prompt"/>
            <a href="/login"><spring:message code="register.login.link"/></a>
        </div>
    </div>
</div>

</body>
</html>