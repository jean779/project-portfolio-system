package br.com.portfolio.repository;

import br.com.portfolio.model.ProjectMember;
import br.com.portfolio.model.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    void deleteAllByProjectId(Long projectId);
}
