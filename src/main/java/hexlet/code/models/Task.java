package hexlet.code.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Temporal;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@Entity
@Setter
@Getter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    
    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name")
    private String name;
    
    @Lob
    @Column(name = "description")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "task_status_id", referencedColumnName = "task_status_id")
    @NotNull(message = "Task status cannot be blank or null")
    private TaskStatus taskStatus;
    
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    @NotBlank(message = "Author cannot be blank")
    private User author;
    
    @ManyToOne
    @JoinColumn(name = "executor_id", referencedColumnName = "user_id")
    private User executor;
    
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAT;
    
    @ManyToMany
    @JoinTable(
            name = "tasks_labels",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private List<Label> labels;
    
    public Task(String name, String description, TaskStatus taskStatus, User author, User executor) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.author = author;
        this.executor = executor;
    }
}
