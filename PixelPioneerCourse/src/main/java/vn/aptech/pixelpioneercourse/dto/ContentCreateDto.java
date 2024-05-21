package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.pixelpioneercourse.entities.ContentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCreateDto {

    @NotNull(message = "Course ID is mandatory")
    private Integer courseId;

    @NotNull(message = "Lesson ID is mandatory")
    private Integer lessonId;

    @NotNull(message = "SubLesson ID is mandatory")
    private Integer subLessonId;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull(message = "Content type is mandatory")
    private ContentType contentType;

    @Positive(message = "Order number must be positive")
    private int orderNumber;

    @NotBlank(message = "Content URL is mandatory")
    private String contentUrl;
}
