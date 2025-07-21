package br.com.portfolio.model;

import br.com.portfolio.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate birthDate;

    private String cpf;

    private boolean employee;

    private boolean manager;

    @Transient
    private RoleType role;

    @OneToMany(mappedBy = "person")
    private List<ProjectMember> projectMemberships;

    @Transient
    public List<String> getProjectNames() {
        if (projectMemberships == null) return List.of();
        return projectMemberships.stream()
                .map(pm -> pm.getProject().getName())
                .toList();
    }

    @PostLoad
    public void defineRole() {
        if (employee) role = RoleType.EMPLOYEE;
        else if (manager) role = RoleType.MANAGER;
        else role = null;
    }

    @Transient
    public String getBirthDateFormatted() {
        return br.com.portfolio.util.DateUtils.format(birthDate);
    }
}
