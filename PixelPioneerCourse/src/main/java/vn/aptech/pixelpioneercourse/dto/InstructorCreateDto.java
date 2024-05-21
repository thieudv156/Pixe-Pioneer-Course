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
public class InstructorCreateDto {

    @NotNull(message = "User ID is mandatory")
    private Integer userId;

    @NotBlank(message = "Information is mandatory")
    @Size(max = 2000, message = "Information must be less than 2000 characters")
    private String information;
}
