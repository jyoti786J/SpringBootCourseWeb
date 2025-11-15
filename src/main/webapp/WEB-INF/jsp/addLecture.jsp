<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="lecture.add.title"/></title>
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
    <a href="<c:url value='/course/index'/>" class="button button-secondary">
        <spring:message code="lecture.cancel.button"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1 class="text-center"><spring:message code="lecture.add.title"/></h1>

        <c:if test="${not empty error}">
            <div class="alert alert-danger mb-3">
                <spring:message code="${error}"/>
            </div>
        </c:if>

        <form method="POST" action="${pageContext.request.contextPath}/course/${courseId}/lecture/add"
              enctype="multipart/form-data" class="lecture-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <label for="lectureNo"><spring:message code="lecture.number.label"/></label>
                <input type="text" id="lectureNo" name="lectureNo" required
                       class="form-control ${not empty errors.lectureNo ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='lecture.number.placeholder'/>">
                <c:if test="${not empty errors.lectureNo}">
                    <div class="error-message">
                        <spring:message code="${errors.lectureNo}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label for="lectureName"><spring:message code="lecture.name.label"/></label>
                <input type="text" id="lectureName" name="lectureName" required
                       class="form-control ${not empty errors.lectureName ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='lecture.name.placeholder'/>">
                <c:if test="${not empty errors.lectureName}">
                    <div class="error-message">
                        <spring:message code="${errors.lectureName}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label for="name"><spring:message code="lecture.displayName.label"/></label>
                <input type="text" id="name" name="name" required
                       class="form-control ${not empty nameError ? 'is-invalid' : ''}"
                       placeholder="<spring:message code='lecture.displayName.placeholder'/>">
                <c:if test="${not empty nameError}">
                    <div class="error-message">
                        <spring:message code="${nameError}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label for="lectureNotes"><spring:message code="lecture.notes.label"/></label>
                <input type="file" id="lectureNotes" name="newLectureNotes" multiple
                       class="form-control ${not empty fileError ? 'is-invalid' : ''}">
                <small class="form-text"><spring:message code="lecture.notes.hint"/></small>
                <c:if test="${not empty fileError}">
                    <div class="error-message">
                        <spring:message code="${fileError}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-plus"></i> <spring:message code="lecture.add.button"/>
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>