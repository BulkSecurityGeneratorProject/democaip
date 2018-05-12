package ro.democaip.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ro.democaip.domain.enumeration.EpicSize;

/**
 * A Epic.
 */
@Entity
@Table(name = "epic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Epic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation")
    private LocalDate creation;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_size")
    private EpicSize size;

    @OneToMany(mappedBy = "epic")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Story> stories = new HashSet<>();

    @ManyToOne
    private User owner;

    @ManyToOne
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Epic name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Epic description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public Epic creation(LocalDate creation) {
        this.creation = creation;
        return this;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }

    public EpicSize getSize() {
        return size;
    }

    public Epic size(EpicSize size) {
        this.size = size;
        return this;
    }

    public void setSize(EpicSize size) {
        this.size = size;
    }

    public Set<Story> getStories() {
        return stories;
    }

    public Epic stories(Set<Story> stories) {
        this.stories = stories;
        return this;
    }

    public Epic addStories(Story story) {
        this.stories.add(story);
        story.setEpic(this);
        return this;
    }

    public Epic removeStories(Story story) {
        this.stories.remove(story);
        story.setEpic(null);
        return this;
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }

    public User getOwner() {
        return owner;
    }

    public Epic owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Project getProject() {
        return project;
    }

    public Epic project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Epic epic = (Epic) o;
        if (epic.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), epic.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Epic{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creation='" + getCreation() + "'" +
            ", size='" + getSize() + "'" +
            "}";
    }
}
