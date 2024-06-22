package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id")
//    @JsonBackReference(value = "course-review")
    @JsonIgnoreProperties({"title", "isPublished", "createdAt", "frontPageImage", "imageUrl","category","description", "price", "instructor", "reviews", "lessons"})
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonBackReference(value = "user-review")
    @JsonIgnoreProperties({"password", "createdAt", "enrollments", "reviews", "discussions", "courses", "tests", "provider", "grantedAuthorities"})
    private User user;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

