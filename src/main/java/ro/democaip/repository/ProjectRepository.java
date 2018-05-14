package ro.democaip.repository;

import ro.democaip.domain.Project;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import ro.democaip.domain.User;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select project from Project project where project.owner.login = ?#{principal.username}")
    List<Project> findByOwnerIsCurrentUser();

    List<Project> getAllByOwner(User user);
}
