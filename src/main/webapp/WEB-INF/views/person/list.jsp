<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ include file="/WEB-INF/views/components/header.jspf" %>
<c:set var="page" value="${peoplePage}"/>
<c:set var="baseUrl" value="/person"/>
<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="people.title"/></title>
</head>
<body class="d-flex flex-column min-vh-100">
<main class="flex-grow-1">
    <div class="container mt-4">
        <h2 class="mb-4"><spring:message code="people.heading"/></h2>

        <table class="table table-striped table-bordered align-middle">
            <thead class="table-dark">
            <tr>
                <th><spring:message code="people.table.name"/></th>
                <th><spring:message code="people.table.birthDate"/></th>
                <th><spring:message code="people.table.cpf"/></th>
                <th><spring:message code="people.table.role"/></th>
                <th><spring:message code="people.table.projects"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="person" items="${peoplePage.content}">
                <tr>
                    <td>${person.name}</td>
                    <td>${person.birthDateFormatted}</td>
                    <td>${person.cpf}</td>
                    <td>
                        <c:choose>
                            <c:when test="${person.role == 'EMPLOYEE'}">
                                <span class="badge bg-success">
                                    <spring:message code="people.role.employee"/>
                                </span>
                            </c:when>
                            <c:when test="${person.role == 'MANAGER'}">
                                <span class="badge bg-primary">
                                    <spring:message code="people.role.manager"/>
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">-</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-outline-secondary"
                                type="button"
                                data-bs-toggle="collapse"
                                data-bs-target="#projects-${person.id}"
                                aria-expanded="false"
                                aria-controls="projects-${person.id}">
                            <i class="bi bi-eye"></i> <spring:message code="people.button.viewProjects"/>
                        </button>
                    </td>
                </tr>

                <tr class="collapse bg-info-subtle" id="projects-${person.id}">
                    <td colspan="5" class="p-0">
                        <div class="p-3">
                            <c:choose>
                                <c:when test="${not empty person.projectMemberships}">
                                    <div class="list-group">
                                        <c:forEach var="membership" items="${person.projectMemberships}">
                                            <c:if test="${membership.project != null}">
                                                <div class="list-group-item list-group-item-action d-flex justify-content-between align-items-start">
                                                    <div class="ms-2 me-auto">
                                                        <div class="fw-bold">${membership.project.name}</div>
                                                        <small>
                                                            <spring:message
                                                                    code="projects.table.status"/>: ${membership.project.status}
                                                            &nbsp;|&nbsp;
                                                            <spring:message
                                                                    code="projects.table.startDate"/>: ${membership.project.startDateFormatted}
                                                        </small>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                <span class="text-muted">
                                    <spring:message code="people.message.noProjects"/>
                                </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </td>
                </tr>


            </c:forEach>
            </tbody>
        </table>

        <%@ include file="/WEB-INF/views/components/pagination.jspf" %>
    </div>
</main>
<%@ include file="/WEB-INF/views/components/footer.jspf" %>
</body>
</html>