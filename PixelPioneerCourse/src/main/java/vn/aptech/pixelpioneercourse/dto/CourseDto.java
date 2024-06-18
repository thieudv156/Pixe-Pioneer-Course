package vn.aptech.pixelpioneercourse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.aptech.pixelpioneercourse.entities.Course;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDto {
    private Integer id;
    private String title;
    private String imageUrl;
    private Double price;
    private String instructorName;

    public CourseDto(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.imageUrl = course.getImageUrl();
        this.price = course.getPrice();
        this.instructorName = course.getInstructor().getFullName();
    }

    // Getters and setters
}
