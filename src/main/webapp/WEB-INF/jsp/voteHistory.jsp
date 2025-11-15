<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="voting.history.title"/></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css" />">
</head>
<body>

<div class="nav">
    <%@ include file="fragments/langSwitcher.jsp" %>
    <a href="/view/${username}" class="button button-secondary">
        <spring:message code="voting.history.back.link"/>
    </a>
</div>

<div class="container">
    <div class="section">
        <h1 class="history-header"><spring:message code="voting.history.title"/></h1>

        <c:choose>
            <c:when test="${empty votes}">
                <div class="empty-history-message">
                    <i class="fas fa-vote-yea"></i> <spring:message code="voting.history.empty"/>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach items="${votes}" var="vote">
                    <div class="vote-item">
                        <div class="vote-question">
                            <i class="fas fa-poll"></i> ${vote.poll.question}
                        </div>
                        <div class="vote-option">
                            <spring:message code="voting.history.you.voted"/>
                            <span>${vote.selectedOption.text}</span>
                        </div>
                        <div class="vote-date">
                            <i class="far fa-clock"></i> <spring:message code="voting.history.voted.on"/>
                            <fmt:formatDate value="${vote.votedAt}" pattern="MMM dd, yyyy hh:mm a"/>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>