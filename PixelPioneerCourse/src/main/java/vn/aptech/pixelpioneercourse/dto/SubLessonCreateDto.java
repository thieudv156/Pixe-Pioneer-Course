package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubLessonCreateDto {

    @NotBlank(message = "Title is required")
    private String title;

    private Integer lessonId;

    @NotBlank(message = "Content is required")
    private String content;
}
