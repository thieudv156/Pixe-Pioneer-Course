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
public class DiscussionCreateDto {
    @NotNull(message = "Sub-lesson ID is mandatory")
    private int subLessonId;

    @NotNull(message = "User ID is mandatory")
    private int userId;

    private Integer parentId;  // Optional field

    @NotBlank(message = "Content is mandatory")
    private String content;

    @NotNull(message = "Creation date is mandatory")
    private LocalDateTime createdAt;
}
