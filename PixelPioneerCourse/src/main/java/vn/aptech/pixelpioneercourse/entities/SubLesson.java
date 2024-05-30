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
@Table(name = "sub_lessons")
public class SubLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column
    private String content;

    @Column
    private boolean completeStatus=false;

    @Column
    private String image;

    @Column
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "subLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discussion> discussions;
}
