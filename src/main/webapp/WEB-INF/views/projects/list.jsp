<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="/WEB-INF/views/components/header.jspf" %>
<c:set var="page" value="${projects}"/>
<c:set var="baseUrl" value="/projects"/>
<!DOCTYPE html>
<html>
<head>
    <title><spring:message code="projects.title"/></title>
</head>
<body class="d-flex flex-column min-vh-100">
<main class="flex-grow-1">
    <div class="container mt-4">
        <h1 class="mb-4"><spring:message code="projects.heading"/></h1>
        <a href="/projects/new" class="btn btn-outline-warning mb-1">
            <i class="bi bi-plus-circle"></i> <spring:message code="projects.button.newProject"/>
        </a>


        <table class="table table-striped table-bordered">
            <thead class="table-dark">
            <tr>
                <th><spring:message code="projects.table.name"/></th>
                <th><spring:message code="projects.table.startDate"/></th>
                <th><spring:message code="projects.table.expectedEnd"/></th>
                <th><spring:message code="projects.table.status"/></th>
                <th><spring:message code="projects.table.risk"/></th>
                <th><spring:message code="projects.table.manager"/></th>
                <th><spring:message code="projects.table.actions"/></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="project" items="${projects.content}">
                <tr>
                    <td>${project.name}</td>
                    <td>${project.startDate}</td>
                    <td>${project.expectedEndDate}</td>
                    <td>
                        <form:form method="post" action="/projects/${project.id}/update-status"
                                   cssClass="d-flex align-items-center status-form">
                            <select name="status"
                                    class="form-select form-select-sm me-1 status-select"
                                    data-project-name="${project.name}">
                                <c:forEach var="s" items="${statuses}">
                                    <option value="${s}" ${s == project.status ? 'selected' : ''}>
                                            ${s}
                                    </option>
                                </c:forEach>
                            </select>
                        </form:form>
                    </td>
                    <td>${project.risk}</td>
                    <td>${project.manager.name}</td>
                    <td>
                        <a href="/projects/${project.id}/edit" class="btn btn-sm btn-outline-warning" title="Edit">
                            <i class="bi bi-pencil-square"></i>
                        </a>
                        <c:if test="${not project.statusLocked}">
                            <button type="button" class="btn btn-sm btn-outline-danger"
                                    data-bs-toggle="modal"
                                    data-bs-target="#confirmDeleteModal"
                                    data-project-id="${project.id}"
                                    data-project-name="${project.name}"
                                    title="Delete">
                                <i class="bi bi-trash"></i>
                            </button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <%@ include file="/WEB-INF/views/components/pagination.jspf" %>
    </div>

    <!-- Modal de confirmação de exclusão -->
    <div class="modal fade" id="confirmDeleteModal" tabindex="-1" aria-labelledby="confirmDeleteLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form:form id="deleteForm" method="post" action="">
                    <div class="modal-header bg-danger text-white">
                        <h5 class="modal-title" id="confirmDeleteLabel">
                            <spring:message code="projects.modal.confirmDelete.title"/>
                        </h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>
                            <spring:message code="projects.modal.confirmDelete.message"/>
                            <strong id="modalProjectName"></strong>?
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <spring:message code="projects.modal.confirmDelete.cancel"/>
                        </button>
                        <button type="submit" id="confirmDeleteButton" class="btn btn-danger">
                            <spring:message code="projects.modal.confirmDelete.confirm"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <div class="modal fade" id="confirmStatusModal" tabindex="-1" aria-labelledby="confirmStatusLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-warning text-dark">
                    <h5 class="modal-title" id="confirmStatusLabel">Confirmar alteração de status</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
                </div>
                <div class="modal-body">
                    Tem certeza que deseja alterar o status do projeto <strong id="modalStatusProjectName"></strong>
                    para <strong id="modalNewStatus"></strong>?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-warning" id="confirmStatusChangeBtn">Confirmar</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // DELETE
            const deleteForm = document.getElementById('deleteForm');
            const modalProjectName = document.getElementById('modalProjectName');
            const deleteButtons = document.querySelectorAll('button[data-project-id]');

            deleteButtons.forEach(button => {
                button.addEventListener('click', () => {
                    const projectId = button.getAttribute('data-project-id');
                    const projectName = button.getAttribute('data-project-name');
                    deleteForm.action = `/projects/\${projectId}/delete`;
                    modalProjectName.textContent = projectName;
                });
            });

            const statusModal = new bootstrap.Modal(document.getElementById('confirmStatusModal'));
            const projectNameLabel = document.getElementById('modalStatusProjectName');
            const newStatusLabel = document.getElementById('modalNewStatus');
            const confirmStatusBtn = document.getElementById('confirmStatusChangeBtn');

            let selectedForm = null;
            let selectedOptionText = '';

            document.querySelectorAll('.status-select').forEach(select => {
                select.addEventListener('change', function () {
                    selectedForm = this.closest('form');
                    const projectName = this.getAttribute('data-project-name');
                    const option = this.options[this.selectedIndex];
                    selectedOptionText = option.text;

                    projectNameLabel.textContent = projectName;
                    newStatusLabel.textContent = selectedOptionText;

                    statusModal.show();
                });
            });

            confirmStatusBtn.addEventListener('click', function () {
                if (selectedForm) selectedForm.submit();
            });
        });
    </script>
</main>
<%@ include file="/WEB-INF/views/components/footer.jspf" %>

</body>
</html>
