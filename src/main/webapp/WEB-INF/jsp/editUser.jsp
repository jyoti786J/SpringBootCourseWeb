<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="admin.user.edit.header" arguments="${username}"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/admin/users'/>" class="button button-secondary">
        <spring:message code="button.cancel"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1><spring:message code="admin.user.edit.header" arguments="${username}"/></h1>

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

        <form:form modelAttribute="user" action="/admin/users/edit/${username}" method="post" class="user-edit-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" name="username" value="${username}"/>

            <div class="form-group">
                <label for="fullName"><spring:message code="user.fullName.label"/></label>
                <form:input path="fullName" id="fullName" class="form-control"
                            placeholder="<spring:message code='user.fullName.placeholder'/>"
                            required="required"/>
                <form:errors path="fullName" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="email"><spring:message code="user.email.label"/></label>
                <form:input path="email" type="email" id="email" class="form-control"
                            placeholder="<spring:message code='user.email.placeholder'/>"/>
                <form:errors path="email" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="phone"><spring:message code="user.phone.label"/></label>
                <form:input path="phone" id="phone" class="form-control"
                            placeholder="<spring:message code='user.phone.placeholder'/>"/>
                <form:errors path="phone" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="role"><spring:message code="user.role.label"/></label>
                <form:select path="role" id="role" class="form-control" required="required">
                    <option value=""><spring:message code="user.role.select"/></option>
                    <c:forEach items="${availableRoles}" var="role">
                        <form:option value="${role}">
                            <spring:message code="role.${role}"/>
                        </form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="role" cssClass="error-message"/>
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-save"></i> <spring:message code="button.update"/>
                </button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>