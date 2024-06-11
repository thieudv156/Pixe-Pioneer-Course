package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({"lessons", "instructor", "category", "enrollments", "frontPageImage", "createdAt","reviews"})
    private Course course;

    @Column
    private Boolean completeStatus=false;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image frontPageImage;

    @Column
    private LocalDateTime createdAt=LocalDateTime.now();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "lesson-test")
    private List<Test> tests;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "lesson-sublesson")
    private List<SubLesson> subLessons;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "lesson-quiz")
    private List<Quiz> quizzes;

    public String getCompleteStatus() {
    	if (completeStatus == null) {
    		return "";
    	} else {
    		return Boolean.toString(completeStatus);
    	}
    }
}
