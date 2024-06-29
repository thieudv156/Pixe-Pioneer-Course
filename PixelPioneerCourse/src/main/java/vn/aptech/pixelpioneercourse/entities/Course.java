package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import vn.aptech.pixelpioneercourse.repository.CourseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"courses", "testFormat"})
    private Category category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"username", "password", "reviews", "discussions", "courses", "enrollments", "tests", "provider", "grantedAuthorities", "authorities", "progresses", "courseCompletes", "phone", "email", "activeStatus", "createdAt", "requestInstructor"})
    @ToString.Exclude
    private User instructor;

    @Column(nullable = false)
    private Boolean isPublished = false;

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image frontPageImage;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnoreProperties("course")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnoreProperties("course")
    @Setter
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("course")
    @ToString.Exclude
    private TestFormat testFormat;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("course")
    @ToString.Exclude
    private List<CourseComplete> courseCompletes = new ArrayList<>();

    public String getImageUrl() {
        if (this.frontPageImage != null) {
            return this.frontPageImage.getImageUrl();
        } else {
            return "https://th.bing.com/th/id/R.26ff8f39241b3a8a90817f04f86d2214?rik=7UPym3r1dnBKIA&pid=ImgRaw&r=0";
        }
    }
}
