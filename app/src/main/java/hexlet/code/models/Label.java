package hexlet.code.models;

import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.TemporalType;
import jakarta.persistence.ManyToMany;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "labels")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Label {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long id;
    
    @Column(name = "name")
    @NotBlank(message = "Name cannot be less than 1 character")
    private String name;
    
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;
    
    @ManyToMany(mappedBy = "labels")
    private List<Task> tasks;
    
    /*public void addTasks(List<Task> tasks) {
        this.tasks = tasks;
        
        for (Task task : tasks) {
            task.getLabels().add(this);
        }
        
    }*/
    
}
