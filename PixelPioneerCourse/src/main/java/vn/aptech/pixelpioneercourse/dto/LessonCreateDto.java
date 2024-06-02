package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonCreateDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    @NotNull(message = "Image cannot be empty")
    private MultipartFile image;

}
