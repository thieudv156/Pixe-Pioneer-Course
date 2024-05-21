package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.pixelpioneercourse.entities.CourseContentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseContentCreateDto {

    @NotNull(message = "Course ID is mandatory")
    private Integer courseId;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotNull(message = "Content type is mandatory")
    private CourseContentType contentType;

    @NotBlank(message = "Content URL is mandatory")
    @Size(max = 1000, message = "Content URL must be less than 1000 characters")
    private String contentUrl;
}
