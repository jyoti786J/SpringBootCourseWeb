<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="lecture.edit.title.ext"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/course/${courseId}/lecture/${lectureId}'/>" class="button button-secondary">
        <spring:message code="lecture.cancel.button.ext"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1><spring:message code="lecture.edit.title.ext"/></h1>

        <form:form method="POST" modelAttribute="lectureForm" class="lecture-edit-form">
            <div class="form-group">
                <%--@declare id="lectureno"--%><label for="lectureNo"><spring:message code="lecture.number.label.ext"/></label>
                <form:input path="lectureNo" type="number" class="form-control"
                            placeholder="<spring:message code='lecture.number.placeholder.ext'/>"/>
                <form:errors path="lectureNo" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <%--@declare id="lecturename"--%><label for="lectureName"><spring:message code="lecture.name.label.ext"/></label>
                <form:input path="lectureName" class="form-control"
                            placeholder="<spring:message code='lecture.name.placeholder.ext'/>"/>
                <form:errors path="lectureName" cssClass="error-message"/>
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-save"></i> <spring:message code="lecture.save.button.ext"/>
                </button>
            </div>
        </form:form>

    </div>
</div>
</body>
</html>