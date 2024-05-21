package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    @NotNull(message = "Course ID is mandatory")
    private Integer courseId;

    @NotNull(message = "User ID is mandatory")
    private Integer userId;

    private Integer parentId;

    @NotBlank(message = "Content is mandatory")
    @Size(max = 1000, message = "Content must be less than 1000 characters")
    private String content;
}
