package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "images")
@Builder
@JsonIgnoreProperties({"lesson", "course", "subLesson"})
public class Image {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    private String imageName;

    private String imageType;

    private String imageUrl;

    @OneToOne(mappedBy = "frontPageImage", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Course course;
}