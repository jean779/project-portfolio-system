package br.com.portfolio.controller;

import br.com.portfolio.enums.ProjectStatus;
import br.com.portfolio.enums.RiskLevel;
import br.com.portfolio.exception.BusinessException;
import br.com.portfolio.model.Project;
import br.com.portfolio.service.PersonService;
import br.com.portfolio.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static br.com.portfolio.util.ViewPaths.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final PersonService personService;
    private final MessageSource messageSource;

    @ModelAttribute
    public void populateCommonAttributes(Model model) {
        model.addAttribute("managers", personService.findAllManagers());
        model.addAttribute("employees", personService.findAllEmployees());
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("risks", RiskLevel.values());
    }

    @GetMapping
    public String listProjects(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {
        Page<Project> projectPage = projectService.getPaginatedProjects(page, size);
        model.addAttribute("projects", projectPage);
        return PROJECT_LIST;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        return PROJECT_FORM;
    }

    @PostMapping
    public String createProject(@Valid @ModelAttribute Project project,
                                BindingResult result,
                                @RequestParam(value = "memberIds", required = false) List<Long> memberIds,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            throw new BusinessException(buildValidationErrorMessage(result));
        }

        projectService.createWithMembers(project, memberIds);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("projects.message.successCreated"));
        return REDIRECT_PROJECTS;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Project project = projectService.findById(id);
        project.extractMemberIds();
        model.addAttribute("project", project);
        return PROJECT_FORM;
    }

    @PostMapping("/{id}/edit")
    public String updateProject(@PathVariable Long id,
                                @Valid @ModelAttribute Project project,
                                BindingResult result,
                                @RequestParam(value = "memberIds", required = false) List<Long> memberIds,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            throw new BusinessException(buildValidationErrorMessage(result));
        }

        projectService.updateWithMembers(id, project, memberIds);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("projects.message.successUpdated"));
        return REDIRECT_PROJECTS;
    }

    @PostMapping("/{id}/update-status")
    public String updateProjectStatus(@PathVariable Long id,
                                      @RequestParam("status") ProjectStatus status,
                                      RedirectAttributes redirectAttributes) {
        projectService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("projects.message.statusUpdated"));
        return REDIRECT_PROJECTS;
    }

    @PostMapping("/{id}/delete")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", getMessage("projects.message.successDeleted"));
        return REDIRECT_PROJECTS;
    }

    private String buildValidationErrorMessage(BindingResult result) {
        StringBuilder errorMessage = new StringBuilder(getMessage("validation.fixErrors"));
        result.getFieldErrors().forEach(err ->
                errorMessage.append("\n - ").append(err.getDefaultMessage())
        );
        return errorMessage.toString();
    }

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
