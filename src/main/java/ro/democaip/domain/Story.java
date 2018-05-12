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

/**
 * A Story.
 */
@Entity
@Table(name = "story")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Story implements Serializable {

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

    @Column(name = "story_point")
    private Integer storyPoint;

    @OneToMany(mappedBy = "story")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> tasks = new HashSet<>();

    @ManyToOne
    private User owner;

    @ManyToOne
    private Epic epic;

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

    public Story name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Story description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public Story creation(LocalDate creation) {
        this.creation = creation;
        return this;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }

    public Integer getStoryPoint() {
        return storyPoint;
    }

    public Story storyPoint(Integer storyPoint) {
        this.storyPoint = storyPoint;
        return this;
    }

    public void setStoryPoint(Integer storyPoint) {
        this.storyPoint = storyPoint;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Story tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Story addTasks(Task task) {
        this.tasks.add(task);
        task.setStory(this);
        return this;
    }

    public Story removeTasks(Task task) {
        this.tasks.remove(task);
        task.setStory(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public User getOwner() {
        return owner;
    }

    public Story owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Epic getEpic() {
        return epic;
    }

    public Story epic(Epic epic) {
        this.epic = epic;
        return this;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
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
        Story story = (Story) o;
        if (story.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), story.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Story{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creation='" + getCreation() + "'" +
            ", storyPoint=" + getStoryPoint() +
            "}";
    }
}
