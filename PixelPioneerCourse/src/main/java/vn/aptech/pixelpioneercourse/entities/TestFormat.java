package vn.aptech.pixelpioneercourse.entities;

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
@Table(name = "test_formats")
public class TestFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column
    private Integer duration;

    @Column
    private Integer totalQuestion;

    @Column
    private Integer passingScore;

    @OneToMany(mappedBy = "testFormat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests;
}
