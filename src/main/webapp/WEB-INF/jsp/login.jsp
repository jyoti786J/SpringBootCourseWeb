<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><spring:message code="login.header"/></title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
  <%@ include file="fragments/langSwitcher.jsp" %>
  <a href="<c:url value='course/index'/>" class="button"><spring:message code="view.course"/></a>
</div>

<div class="login-container">
  <div class="section text-center">
    <h1><spring:message code="login.header"/></h1>

    <c:if test="${not empty error}">
      <div class="error-message mb-3"><spring:message code="login.error"/></div>
    </c:if>

    <form action="/login" method="post" class="login-form">
      <div class="form-group">
        <input type="text" name="username" placeholder="<spring:message code="login.username.placeholder"/>" required>
      </div>
      <div class="form-group">
        <input type="password" name="password" placeholder="<spring:message code="login.password.placeholder"/>" required>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      <button type="submit" class="button button-primary"><spring:message code="login.button"/></button>
    </form>

    <div class="mt-3">
      <a href="/register" class="register-link"><spring:message code="login.register.link"/></a>
    </div>
  </div>
</div>

</body>
</html>