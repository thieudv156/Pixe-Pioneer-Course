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

    @Column(name = "content" , columnDefinition = "TEXT")
    private String content;

    @Column(name = "image")
    private String image;

    @Column(name="completed_status")
    private boolean completedStatus = false;

    @Column
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "subLesson", cascade = CascadeType.ALL)
    private List<Discussion> discussions;
}
