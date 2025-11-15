<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="poll.edit.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/course/${courseId}/poll/${pollId}'/>" class="button button-secondary">
        <spring:message code="poll.cancel.button"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1><spring:message code="poll.edit.title"/></h1>

        <form:form modelAttribute="pollForm" method="post"
                   action="/course/${courseId}/poll/${pollId}/edit" class="poll-edit-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="form-group">
                <label for="question"><spring:message code="poll.question.label"/></label>
                <form:input path="question" type="text" id="question" required="required"
                            placeholder="<spring:message code='poll.question.placeholder'/>"/>
                <form:errors path="question" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="option1"><spring:message code="poll.option.label"/> 1</label>
                <form:input path="option1" type="text" id="option1" required="required"
                            placeholder="<spring:message code='poll.option.placeholder'/>"/>
                <form:errors path="option1" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="option2"><spring:message code="poll.option.label"/> 2</label>
                <form:input path="option2" type="text" id="option2" required="required"
                            placeholder="<spring:message code='poll.option.placeholder'/>"/>
                <form:errors path="option2" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="option3"><spring:message code="poll.option.label"/> 3</label>
                <form:input path="option3" type="text" id="option3" required="required"
                            placeholder="<spring:message code='poll.option.placeholder'/>"/>
                <form:errors path="option3" cssClass="error-message"/>
            </div>

            <div class="form-group">
                <label for="option4"><spring:message code="poll.option.label"/> 4</label>
                <form:input path="option4" type="text" id="option4" required="required"
                            placeholder="<spring:message code='poll.option.placeholder'/>"/>
                <form:errors path="option4" cssClass="error-message"/>
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-save"></i> <spring:message code="poll.update.button"/>
                </button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>