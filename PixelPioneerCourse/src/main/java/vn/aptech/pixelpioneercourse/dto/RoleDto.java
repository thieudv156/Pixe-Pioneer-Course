package vn.aptech.pixelpioneercourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RoleDto {
    public Integer id;
    public String roleName;

    @JsonIgnoreProperties({"password"})
    private List<UserDto> users;
}