package vn.aptech.pixelpioneercourse.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "images")
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    private String imageName;

    private String imageType;

    private String imageUrl;

    @OneToOne(mappedBy = "frontPageImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Lesson lesson;

    @OneToOne(mappedBy = "frontPageImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Course course;

    @OneToOne(mappedBy = "frontPageImage", cascade = CascadeType.ALL, orphanRemoval = true)
    private SubLesson subLesson;
}