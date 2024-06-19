package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "discussions")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Discussion {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sub_lesson_id")
    @JsonIgnoreProperties({"lesson","enrollment", "content"})
    private SubLesson subLesson;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"enrollments", "createdAt", "password", "phone", "courses", "tests", "provider", "grantedAuthorities", "reviews"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
//    @JsonBackReference(value = "parent-discussion")
    private Discussion parent;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "parent-discussion")
    private List<Discussion> children;

}
