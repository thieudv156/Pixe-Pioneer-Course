package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubLessonCreateDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Lesson ID is required")
    private Integer lessonId;

    private String content;

    private String image;

    @NotNull(message = "Created at date is required")
    private LocalDateTime createdAt;
}
