<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="profile.edit.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/view/${user.username}'/>" class="button button-secondary">
        <spring:message code="profile.cancel.button"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1><spring:message code="profile.edit.title"/></h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <spring:message code="${error}"/>
            </div>
        </c:if>

        <form:form modelAttribute="user" method="post" class="profile-edit-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <label><spring:message code="profile.username.label"/></label>
                <div class="input-icon">
                    <i class="fas fa-user"></i>
                    <form:input path="username" cssClass="form-control" readonly="true"/>
                </div>
            </div>

            <div class="form-group">
                <label><spring:message code="profile.fullName.label"/></label>
                <div class="input-icon">
                    <i class="fas fa-id-card"></i>
                    <form:input path="fullName" cssClass="form-control"
                                placeholder="<spring:message code='profile.fullName.placeholder'/>"
                                required="required"/>
                </div>
                <form:errors path="fullName" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label><spring:message code="profile.email.label"/></label>
                <div class="input-icon">
                    <i class="fas fa-envelope"></i>
                    <form:input path="email" type="email" cssClass="form-control"
                                placeholder="<spring:message code='profile.email.placeholder'/>"
                                required="required"/>
                </div>
                <form:errors path="email" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label><spring:message code="profile.phone.label"/></label>
                <div class="input-icon">
                    <i class="fas fa-phone"></i>
                    <form:input path="phone" type="tel" cssClass="form-control"
                                placeholder="<spring:message code='profile.phone.placeholder'/>"
                                required="required"/>
                </div>
                <form:errors path="phone" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label><spring:message code="register.newPassword"/></label><span class="password-toggle" onclick="togglePassword(this)">
                        <i class="fas fa-eye"></i>
                    </span>
                <small class="form-text"><spring:message code="register.password.optional"/></small>
                <div class="input-icon">
                    <i class="fas fa-lock"></i>
                    <input type="password" id="newPassword" name="newPassword" class="form-control"
                           placeholder="<spring:message code='register.newPassword.placeholder'/>"/>
                </div>
            </div>

            <div class="form-group" id="confirmPasswordGroup">
                <label><spring:message code="register.confirmPassword"/></label><span class="password-toggle" onclick="togglePassword(this)">
                        <i class="fas fa-eye"></i>
                    </span>
                <div class="input-icon">
                    <i class="fas fa-lock"></i>
                    <input type="password" id="confirmPassword" name="confirmPassword"
                           class="form-control"
                           placeholder="<spring:message code='register.confirmPassword.placeholder'/>"/>
                </div>
                <div id="passwordMatchError" class="error-message" style="display: none;">
                    <i class="fas fa-exclamation-circle"></i>
                    <span><spring:message code="register.password.mismatch"/></span>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-save"></i> <spring:message code="profile.update.button"/>
                </button>
            </div>
        </form:form>
    </div>
</div>

<script>
    // Show/hide password
    function togglePassword(element) {
        const input = element.closest('.input-icon').querySelector('input');
        const icon = element.querySelector('i');

        if (input.type === 'password') {
            input.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            input.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    }

    // Validate password match
    document.querySelector('form').addEventListener('submit', function(e) {
        const password = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const errorElement = document.getElementById('passwordMatchError');

        if (password && password !== confirmPassword) {
            errorElement.style.display = 'flex';
            e.preventDefault();
        } else {
            errorElement.style.display = 'none';
        }
    });

    // Show confirm password field only if password field has value
    document.getElementById('newPassword').addEventListener('input', function(e) {
        const confirmGroup = document.getElementById('confirmPasswordGroup');
        if (e.target.value) {
            confirmGroup.style.display = 'block';
        } else {
            confirmGroup.style.display = 'none';
        }
    });
</script>
</body>
</html>