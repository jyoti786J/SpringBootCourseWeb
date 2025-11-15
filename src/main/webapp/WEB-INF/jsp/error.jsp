<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error Page</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="<c:url value='/course/index'/>" class="button button-secondary">
        <spring:message code="button.back"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1 class="text-center"><spring:message code="error.page.title"/></h1>

        <c:choose>
            <c:when test="${empty message}">
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle"></i> <spring:message code="error.generic.message"/>
                </div>
                <div class="error-details">
                    <p><strong><spring:message code="error.status.code"/>:</strong> ${status}</p>
                    <p><strong><spring:message code="error.exception"/>:</strong> ${exception}</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle"></i> ${message}
                </div>
            </c:otherwise>
        </c:choose>

        <div class="text-center mt-4">
            <a href="<c:url value='/course/index'/>" class="button button-primary">
                <i class="fas fa-home"></i> <spring:message code="error.return.home"/>
            </a>
        </div>
    </div>
</div>
</body>
</html>