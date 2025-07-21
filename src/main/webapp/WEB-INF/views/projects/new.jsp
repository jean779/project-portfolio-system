<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ include file="/WEB-INF/views/components/header.jspf" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="project.new.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/tom-select@2.3.1/dist/css/tom-select.bootstrap5.min.css" rel="stylesheet">
</head>
<body class="d-flex flex-column min-vh-100">

<main class="flex-grow-1">
    <div class="container mt-5">
        <h2 class="mb-4">
            <spring:message code="project.new.heading"/>
        </h2>

        <div class="card shadow-sm border-0">
            <div class="card-body">
                <c:choose>
                    <c:when test="${project.id != null}">
                        <c:url var="formAction" value="/projects/${project.id}/edit"/>
                    </c:when>
                    <c:otherwise>
                        <c:url var="formAction" value="/projects"/>
                    </c:otherwise>
                </c:choose>
                <form:form modelAttribute="project" action="${formAction}" method="post" cssClass="needs-validation"
                           novalidate="novalidate">
                    <form:hidden path="id"/>
                    <div class="row">
                        <div class="mb-3 col-md-6">
                            <label for="name" class="form-label">
                                <spring:message code="project.new.name.label"/>
                            </label>
                            <spring:message code="project.new.name.placeholder" var="namePlaceholder"/>
                            <form:input path="name"
                                        id="name"
                                        cssClass="form-control"
                                        placeholder="${namePlaceholder}"/>
                            <form:errors path="name" cssClass="invalid-feedback"/>
                        </div>
                        <div class="mb-3 col-md-6">
                            <label for="budget" class="form-label"><spring:message
                                    code="project.new.budget.label"/></label>
                            <form:input path="budget" id="budget" cssClass="form-control" type="number" step="0.01"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="mb-3 col-md-6">
                            <label for="startDate" class="form-label"><spring:message
                                    code="project.new.startDate.label"/></label>
                            <c:set var="startDateRequired"
                                   value='<spring:message code="project.new.startDate.required"/>'/>
                            <form:input path="startDate" id="startDate" type="date" cssClass="form-control"
                                        oninvalid="this.setCustomValidity('${startDateRequired}')"
                                        oninput="this.setCustomValidity('')"/>
                            <form:errors path="startDate" cssClass="invalid-feedback"/>
                        </div>
                        <div class="mb-3 col-md-6">
                            <label for="expectedEndDate" class="form-label">
                                <spring:message code="project.new.expectedEndDate.label"/>
                            </label>
                            <form:input path="expectedEndDate" id="expectedEndDate" type="date"
                                        cssClass="form-control"/>
                            <form:errors path="expectedEndDate" cssClass="invalid-feedback"/>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">
                            <spring:message code="project.new.description.label"/>
                        </label>
                        <form:textarea path="description" id="description" cssClass="form-control" rows="3"/>
                        <form:errors path="description" cssClass="invalid-feedback"/>
                    </div>

                    <div class="row">
                        <div class="mb-3 col-md-6">
                            <label for="status" class="form-label">
                                <spring:message code="project.new.status.label"/>
                            </label>
                            <form:select path="status" id="status" cssClass="form-select">
                                <form:options items="${statuses}"/>
                            </form:select>
                            <form:errors path="status" cssClass="invalid-feedback"/>
                        </div>

                        <div class="mb-3 col-md-6">
                            <label for="risk" class="form-label">
                                <spring:message code="project.new.risk.label"/>
                            </label>
                            <form:select path="risk" id="risk" cssClass="form-select">
                                <form:options items="${risks}"/>
                            </form:select>
                            <form:errors path="risk" cssClass="invalid-feedback"/>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="managerSelect" class="form-label">
                            <spring:message code="project.new.manager.label"/>
                        </label>
                        <c:set var="managerRequired" value='<spring:message code="project.new.manager.required"/>'/>
                        <form:select path="manager" id="managerSelect" cssClass="form-select"
                                     oninvalid="this.setCustomValidity('${managerRequired}')"
                                     oninput="this.setCustomValidity('')">
                            <form:options items="${managers}" itemValue="id" itemLabel="name"/>
                        </form:select>
                        <form:errors path="manager" cssClass="invalid-feedback"/>
                    </div>

                    <div class="mb-3">
                        <label for="memberSelect" class="form-label">
                            <spring:message code="project.new.members.label"/>
                        </label>
                        <form:select path="memberIds" id="memberSelect" multiple="true" cssClass="form-select">
                            <form:options items="${employees}" itemValue="id" itemLabel="name"/>
                        </form:select>
                        <form:errors path="memberIds" cssClass="invalid-feedback"/>
                    </div>

                    <div class="d-flex justify-content-end mt-4">
                        <a href="/projects" class="btn btn-outline-secondary me-2">
                            <spring:message code="project.new.back"/>
                        </a>
                        <button type="submit" class="btn btn-outline-success">
                            <i class="bi bi-check-circle"></i> <spring:message code="project.new.submit"/>
                        </button>
                    </div>

                </form:form>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/tom-select@2.3.1/dist/js/tom-select.complete.min.js"></script>
<script>
    new TomSelect('#managerSelect', {
        create: false,
        placeholder: '<spring:message code="project.new.manager.placeholder"/>',
        maxItems: 1
    });

    new TomSelect('#memberSelect', {
        create: false,
        plugins: ['remove_button'],
        placeholder: '<spring:message code="project.new.members.placeholder"/>'
    });
</script>

<%@ include file="/WEB-INF/views/components/footer.jspf" %>
</body>
</html>
