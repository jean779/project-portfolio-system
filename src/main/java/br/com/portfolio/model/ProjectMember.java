package br.com.portfolio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "membros")
@ToString(exclude = {"project", "person"})
@Data
@NoArgsConstructor
public class ProjectMember {

    @EmbeddedId
    private ProjectMemberId id = new ProjectMemberId();

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "idprojeto")
    private Project project;

    @ManyToOne
    @MapsId("personId")
    @JoinColumn(name = "idpessoa")
    private Person person;

    public ProjectMember(Project project, Person person) {
        this.project = project;
        this.person = person;
        this.id = new ProjectMemberId(project.getId(), person.getId());
    }
}

