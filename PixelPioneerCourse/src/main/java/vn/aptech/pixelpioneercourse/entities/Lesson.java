package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "lessons")
@JsonIgnoreProperties({"tests", "quizzes"})
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({"lessons", "instructor", "category", "enrollments", "frontPageImage", "createdAt","reviews","price","description","isPublished","imageUrl"})
    private Course course;

    @Column
    private LocalDateTime createdAt=LocalDateTime.now();

    @Column
    private Integer orderNumber;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"lesson","createdAt","discussions","progresses"})
    @ToString.Exclude
    private List<SubLesson> subLessons = new ArrayList<>();
}
