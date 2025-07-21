<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/views/components/header.jspf" %>

<div class="container mt-5 text-center">
    <h1 class="text-danger display-1">
        <spring:message code="error.title" />
    </h1>
    <h2 class="mb-3">
        <spring:message code="error.heading" />
    </h2>
    <p class="lead">
        <spring:message code="error.description" />
    </p>

    <div class="mt-4">
        <c:if test="${not empty status}">
            <p><strong><spring:message code="error.code.label" />:</strong> ${status}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p><strong><spring:message code="error.message.label" />:</strong> ${error}</p>
        </c:if>
        <c:if test="${not empty path}">
            <p><strong><spring:message code="error.path.label" />:</strong> ${path}</p>
        </c:if>
    </div>

    <a href="/" class="btn btn-outline-primary mt-4">
        <spring:message code="error.backToHome" />
    </a>
</div>
