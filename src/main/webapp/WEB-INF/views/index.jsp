<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="/WEB-INF/views/components/header.jspf" %>

<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="index.title"/></title>
</head>
<body class="d-flex flex-column min-vh-100">
<main class="flex-grow-1">

    <div class="container mt-5 text-center">
        <h1 class="text-primary mb-3">
            <spring:message code="index.heading"/>
        </h1>
        <p class="lead text-muted">
            <spring:message code="index.subtitle"/>
        </p>
    </div>
</main>
<%@ include file="/WEB-INF/views/components/footer.jspf" %>
</body>
</html>
