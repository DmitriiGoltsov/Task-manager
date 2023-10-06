package hexlet.code.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.FetchType;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "labels")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @NotBlank(message = "Name cannot be less than 1 character")
    private String name;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "labels")
    private Set<Task> tasks;
}
