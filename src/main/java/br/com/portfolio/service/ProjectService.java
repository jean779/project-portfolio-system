package br.com.portfolio.service;

import br.com.portfolio.enums.ProjectStatus;
import br.com.portfolio.exception.BusinessException;
import br.com.portfolio.model.Person;
import br.com.portfolio.model.Project;
import br.com.portfolio.model.ProjectMember;
import br.com.portfolio.repository.PersonRepository;
import br.com.portfolio.repository.ProjectMemberRepository;
import br.com.portfolio.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public Page<Project> getPaginatedProjects(int page, int size) {
        log.debug("Fetching paginated projects - page={}, size={}", page, size);
        return projectRepository.findAll(PageRequest.of(page, size));
    }

    public Project findById(Long id) {
        log.debug("Looking for project with ID {}", id);
        return projectRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Project with ID {} not found", id);
                    return new BusinessException("Project not found.");
                });
    }

    @Transactional
    public void delete(Long id) {
        log.info("Attempting to delete project with ID {}", id);
        Project project = findById(id);
        if (project.getStatus().isLocked()) {
            log.warn("Cannot delete project ID={} with locked status {}", id, project.getStatus());
            throw new BusinessException("Cannot delete a project with status: " + project.getStatus());
        }
        projectRepository.delete(project);
        log.info("Project ID={} deleted successfully", id);
    }

    @Transactional
    public void createWithMembers(Project project, List<Long> memberIds) {
        log.info("Creating new project '{}' with memberIds={}", project.getName(), memberIds);
        if (project.getManager() == null || project.getManager().getId() == null) {
            log.warn("Manager is missing when creating project");
            throw new BusinessException("Manager is required to create a project.");
        }
        Person manager = findManagerOrThrow(project.getManager().getId());
        project.setManager(manager);
        Project savedProject = projectRepository.save(project);
        log.debug("Project '{}' saved with ID {}", savedProject.getName(), savedProject.getId());
        saveMembers(savedProject, memberIds);
        log.info("Project '{}' created and members associated successfully", savedProject.getName());
    }

    @Transactional
    public void updateWithMembers(Long id, Project updatedData, List<Long> memberIds) {
        log.info("Updating project ID={} with memberIds={}", id, memberIds);
        Project existing = findById(id);
        applyUpdates(existing, updatedData);
        if (updatedData.getManager() == null || updatedData.getManager().getId() == null) {
            log.warn("Manager is missing in the project update data");
            throw new BusinessException("Manager is required.");
        }
        Person manager = findManagerOrThrow(updatedData.getManager().getId());
        existing.setManager(manager);

        projectMemberRepository.deleteAllByProjectId(existing.getId());
        saveMembers(existing, memberIds);
        log.info("Project ID={} updated successfully", id);
    }

    private void applyUpdates(Project target, Project source) {
        log.debug("Applying updates to project ID={}", target.getId());
        target.setName(source.getName());
        target.setStartDate(source.getStartDate());
        target.setExpectedEndDate(source.getExpectedEndDate());
        target.setEndDate(source.getEndDate());
        target.setDescription(source.getDescription());
        target.setStatus(source.getStatus());
        target.setRisk(source.getRisk());
        target.setBudget(source.getBudget());
    }

    private void saveMembers(Project project, List<Long> memberIds) {
        List<Person> members = memberIds == null ? Collections.emptyList() : personRepository.findAllById(memberIds);
        log.debug("Saving members for project ID={}: {}", project.getId(), memberIds);

        boolean hasNonEmployee = members.stream().anyMatch(p -> !p.isEmployee());
        if (hasNonEmployee) {
            log.error("Invalid member assignment: one or more persons are not employees.");
            throw new BusinessException("Only employees can be assigned as project members.");
        }

        List<ProjectMember> associations = members.stream()
                .map(member -> new ProjectMember(project, member))
                .collect(Collectors.toList());

        projectMemberRepository.saveAll(associations);
        log.info("{} members associated with project '{}'", associations.size(), project.getName());
    }

    private Person findManagerOrThrow(Long managerId) {
        log.debug("Fetching manager with ID {}", managerId);
        return personRepository.findById(managerId)
                .orElseThrow(() -> {
                    log.warn("Manager with ID {} not found", managerId);
                    return new BusinessException("Manager not found.");
                });
    }

    @Transactional
    public void updateStatus(Long id, ProjectStatus newStatus) {
        log.info("Updating status for project ID={} to {}", id, newStatus);
        Project project = findById(id);
        project.setStatus(newStatus);
        log.info("Status of project ID={} updated to {}", id, newStatus);
    }

}
