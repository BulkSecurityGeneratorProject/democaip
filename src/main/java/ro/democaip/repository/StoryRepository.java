package ro.democaip.repository;

import ro.democaip.domain.Story;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Story entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    @Query("select story from Story story where story.owner.login = ?#{principal.username}")
    List<Story> findByOwnerIsCurrentUser();

}
