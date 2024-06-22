package vn.aptech.pixelpioneercourse.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(columnDefinition = "TEXT")
    private String question="Question";

    @Column(columnDefinition = "TEXT")
    private String correctAnswer="Correct Answer";

    @Column(columnDefinition = "TEXT")
    private String wrongAnswer1="Wrong Answer 1";

    @Column(columnDefinition = "TEXT")
    private String wrongAnswer2="Wrong Answer 2";

    @Column(columnDefinition = "TEXT")
    private String wrongAnswer3="Wrong Answer 3";
}
