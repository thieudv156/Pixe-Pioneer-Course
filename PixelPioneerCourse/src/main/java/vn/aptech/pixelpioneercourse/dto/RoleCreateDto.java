package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleCreateDto {
    @NotBlank(message = "Role name cannot be blank")
    private String name;
}
