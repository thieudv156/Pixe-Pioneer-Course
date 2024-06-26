package vn.aptech.pixelpioneercourse.entities;

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
@Table(name = "course_complete")
public class CourseComplete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties({"lessons", "instructor", "category", "enrollments", "frontPageImage", "createdAt","reviews","price","description","isPublished","imageUrl", "title", "testFormat","courseCompletes"})
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"id", "username", "password", "reviews", "discussions", "courses", "enrollments", "tests", "provider", "grantedAuthorities", "authorities", "progresses", "courseCompletes","phone","email","activeStatus","createdAt","requestInstructor"})
    private User user;

    @Column
    private LocalDateTime completedDate = LocalDateTime.now();
}