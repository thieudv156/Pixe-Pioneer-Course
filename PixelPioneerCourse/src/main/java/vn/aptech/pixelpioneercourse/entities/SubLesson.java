package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "sub_lessons")
public class SubLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonIgnoreProperties({"subLessons", "tests", "quizzes", "instructor", "category", "enrollments", "frontPageImage", "createdAt","reviews","orderNumber"})
    private Lesson lesson;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column()
    private Integer orderNumber;

    @Column(name="created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "subLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"subLesson", "user"})
    private List<Discussion> discussions;

    @OneToMany(mappedBy = "subLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"subLesson", "user"})
    private List<Progress> progresses = new ArrayList<>();

}
