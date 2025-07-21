package br.com.portfolio.service;

import br.com.portfolio.enums.ProjectStatus;
import br.com.portfolio.exception.BusinessException;
import br.com.portfolio.model.Person;
import br.com.portfolio.model.Project;
import br.com.portfolio.repository.PersonRepository;
import br.com.portfolio.repository.ProjectMemberRepository;
import br.com.portfolio.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProjectWithValidManagerAndEmployees() {
        Project project = new Project();
        Long managerId = 1L;
        Long employeeId = 2L;

        Person manager = new Person();
        manager.setId(managerId);
        manager.setManager(true);
        project.setManager(manager);

        Person employee = new Person();
        employee.setId(employeeId);
        employee.setEmployee(true);

        when(personRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(projectRepository.save(project)).thenReturn(project);
        when(personRepository.findAllById(Collections.singletonList(employeeId)))
                .thenReturn(Collections.singletonList(employee));

        projectService.createWithMembers(project, Collections.singletonList(employeeId));

        verify(projectRepository).save(project);
        verify(projectMemberRepository).saveAll(anyList());
    }

    @Test
    void shouldThrowWhenAssigningNonEmployeeAsMember() {
        Project project = new Project();
        Long managerId = 1L;
        Long nonEmployeeId = 2L;

        Person manager = new Person();
        manager.setId(managerId);
        manager.setManager(true);

        Person nonEmployee = new Person();
        nonEmployee.setId(nonEmployeeId);
        nonEmployee.setEmployee(false);

        when(personRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(projectRepository.save(project)).thenReturn(project);
        when(personRepository.findAllById(Collections.singletonList(nonEmployeeId)))
                .thenReturn(Collections.singletonList(nonEmployee));

        assertThrows(BusinessException.class, () ->
                projectService.createWithMembers(project, Collections.singletonList(nonEmployeeId)));
    }

    @Test
    void shouldDeleteProjectWhenStatusIsAllowed() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setStatus(ProjectStatus.PLANNED);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.delete(projectId);

        verify(projectRepository).delete(project);
    }

    @Test
    void shouldNotDeleteProjectWithLockedStatus() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setStatus(ProjectStatus.FINISHED);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(BusinessException.class, () -> projectService.delete(projectId));
        verify(projectRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateProjectWithValidManagerAndEmployees() {
        Long projectId = 1L;
        Long managerId = 2L;
        Long memberId = 3L;

        Person manager = new Person();
        manager.setId(managerId);
        manager.setManager(true);

        Person member = new Person();
        member.setId(memberId);
        member.setEmployee(true);

        Project existing = new Project();
        existing.setId(projectId);

        Project updated = new Project();
        updated.setName("Updated Project");
        updated.setStartDate(LocalDate.now());
        updated.setBudget(1000.0);
        updated.setManager(manager);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existing));
        when(personRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(personRepository.findAllById(Collections.singletonList(memberId)))
                .thenReturn(Collections.singletonList(member));

        projectService.updateWithMembers(projectId, updated, Collections.singletonList(memberId));

        verify(projectMemberRepository).deleteAllByProjectId(projectId);
        verify(projectMemberRepository).saveAll(anyList());
    }

    @Test
    void shouldThrowWhenManagerNotFoundDuringUpdate() {
        Long projectId = 1L;
        Long invalidManagerId = 99L;

        Project existing = new Project();
        existing.setId(projectId);

        Project updated = new Project();
        Person fakeManager = new Person();
        fakeManager.setId(invalidManagerId);
        updated.setManager(fakeManager);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existing));
        when(personRepository.findById(invalidManagerId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () ->
                projectService.updateWithMembers(projectId, updated, Collections.emptyList()));
    }

    @Test
    void shouldThrowWhenAssigningInvalidMemberOnUpdate() {
        Long projectId = 1L;
        Long managerId = 2L;
        Long nonEmployeeId = 3L;

        Person manager = new Person();
        manager.setId(managerId);
        manager.setManager(true);

        Person nonEmployee = new Person();
        nonEmployee.setId(nonEmployeeId);
        nonEmployee.setEmployee(false);

        Project existing = new Project();
        existing.setId(projectId);

        Project updated = new Project();
        updated.setManager(manager);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existing));
        when(personRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(personRepository.findAllById(Collections.singletonList(nonEmployeeId)))
                .thenReturn(Collections.singletonList(nonEmployee));

        assertThrows(BusinessException.class, () ->
                projectService.updateWithMembers(projectId, updated, Collections.singletonList(nonEmployeeId)));
    }
}
