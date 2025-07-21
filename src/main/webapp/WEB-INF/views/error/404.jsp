<%@ page isErrorPage="true" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/components/header.jspf" %>

<div class="container mt-5 text-center">
    <h1 class="display-1 text-warning">404</h1>
    <h2 class="mb-3">Page not found</h2>
    <p class="lead">The page you are trying to access does not exist or may have been removed.</p>

    <c:if test="${not empty path}">
        <p><strong>Requested URL:</strong> ${path}</p>
    </c:if>
</div>
