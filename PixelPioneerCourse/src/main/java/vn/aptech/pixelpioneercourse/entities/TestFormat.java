package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @JsonIgnoreProperties({"testFormat"})
    @ToString.Exclude
    private Course course;

    @Column
    private Integer duration = 300;

    @Column
    private Integer totalQuestion = 1;

    @Column
    private Integer passingScore = 60;
}