<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="poll.create.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/course/index'/>" class="button button-secondary">
        <spring:message code="poll.back.link"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1><spring:message code="poll.create.title"/></h1>

        <form method="POST" action="/course/${courseId}/poll/create" class="poll-form">
            <security:csrfInput />

            <div class="form-group">
                <label><spring:message code="poll.question.label"/></label>
                <input type="text" name="question" required
                       placeholder="<spring:message code='poll.question.placeholder'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="poll.option.label" arguments="1"/></label>
                <input type="text" name="option1" required
                       placeholder="<spring:message code='poll.option.placeholder' arguments='1'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="poll.option.label" arguments="2"/></label>
                <input type="text" name="option2" required
                       placeholder="<spring:message code='poll.option.placeholder' arguments='2'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="poll.option.label" arguments="3"/></label>
                <input type="text" name="option3" required
                       placeholder="<spring:message code='poll.option.placeholder' arguments='3'/>">
            </div>

            <div class="form-group">
                <label><spring:message code="poll.option.label" arguments="4"/></label>
                <input type="text" name="option4" required
                       placeholder="<spring:message code='poll.option.placeholder' arguments='4'/>">
            </div>

            <div class="form-actions">
                <button type="submit" class="button button-primary">
                    <i class="fas fa-plus"></i> <spring:message code="poll.create.button"/>
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>