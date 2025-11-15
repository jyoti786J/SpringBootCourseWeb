<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="lecture.note.add.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/course/${course.id}/lecture/${lecture.id}'/>" class="button button-secondary">
        <spring:message code="lecture.note.cancel.button"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <div class="card-header">
            <h2 class="card-title">
                <i class="fas fa-file-upload"></i> <spring:message code="lecture.note.add.heading" arguments="${lecture.lectureNo}"/>
                <i class="fas fa-chalkboard-teacher"></i> <c:out value="${lecture.lectureName}"/>
            </h2>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success mb-3">
                <i class="fas fa-check-circle"></i> <spring:message code="${successMessage}"/>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger mb-3">
                <i class="fas fa-exclamation-circle"></i> <spring:message code="${errorMessage}"/>
            </div>
        </c:if>

        <form method="post" enctype="multipart/form-data"
              action="${pageContext.request.contextPath}/course/${course.id}/lecture/${lecture.id}/note/add"
              class="lecture-note-form">

            <div class="form-group">
                <label for="file" class="form-label">
                    <i class="fas fa-file-alt"></i> <spring:message code="lecture.note.file.label"/>
                </label>
                <input type="file" id="file" name="file" class="form-input" required>
                <small class="form-text"><spring:message code="lecture.note.file.hint"/></small>
                <c:if test="${not empty fileError}">
                    <div class="form-error">
                        <i class="fas fa-exclamation-circle"></i> <spring:message code="${fileError}"/>
                    </div>
                </c:if>
            </div>

            <div class="form-group">
                <label for="name" class="form-label">
                    <i class="fas fa-tag"></i> <spring:message code="lecture.note.name.label"/>
                </label>
                <input type="text" id="name" name="name" class="form-input"
                       placeholder="<spring:message code='lecture.note.name.placeholder'/>" required>
                <c:if test="${not empty nameError}">
                    <div class="form-error">
                        <i class="fas fa-exclamation-circle"></i> <spring:message code="${nameError}"/>
                    </div>
                </c:if>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-upload"></i> <spring:message code="lecture.note.upload.button"/>
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        const fileInput = document.getElementById('file');
        const maxSize = 10 * 1024 * 1024; // 10MB
        const sizeError = "<spring:message code='lecture.note.file.size.error' javaScriptEscape='true'/>";

        if (fileInput.files.length > 0) {
            if (fileInput.files[0].size > maxSize) {
                e.preventDefault();
                alert(sizeError);
            }
        }
    });
</script>
</body>
</html>