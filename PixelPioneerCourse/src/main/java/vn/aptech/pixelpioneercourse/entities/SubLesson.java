package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonBackReference(value = "lesson-sublesson")
    private Lesson lesson;
    
    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @Column
    private String content;

    @Column
    private Boolean completeStatus=false;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image frontPageImage;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "subLesson", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "sublesson-discussion")
    private List<Discussion> discussions;
    
    public String getCompleteStatus() {
    	if (completeStatus == null) {
    		return "";
    	} else {
    		return Boolean.toString(completeStatus);
    	}
    }
}
