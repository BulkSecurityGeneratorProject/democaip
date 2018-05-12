package ro.democaip.repository;

import ro.democaip.domain.Epic;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Epic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {

    @Query("select epic from Epic epic where epic.owner.login = ?#{principal.username}")
    List<Epic> findByOwnerIsCurrentUser();

}
