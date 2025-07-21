package br.com.portfolio.model;

import br.com.portfolio.enums.ProjectStatus;
import br.com.portfolio.util.DateUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{project.new.name.required}")
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "{project.new.startDate.required}")
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate expectedEndDate;

    private LocalDate endDate;

    @Size(max = 5000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @NotNull(message = "{project.new.budget.required}")
    @Positive(message = "{project.new.budget.positive}")
    private Double budget;

    @NotBlank
    private String risk;

    @ManyToOne
    @JoinColumn(name = "idgerente", nullable = false)
    private Person manager;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMember> members;

    @Transient
    private List<Long> memberIds;

    public Boolean getStatusLocked() {
        return status != null && status.isLocked();
    }

    public void extractMemberIds() {
        if (members != null) {
            this.memberIds = members.stream()
                    .map(pm -> pm.getPerson().getId())
                    .toList();
        }
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", budget=" + budget +
                ", status=" + status +
                '}';
    }

    @Transient
    public String getStartDateFormatted() {
        return DateUtils.format(startDate);
    }
}
