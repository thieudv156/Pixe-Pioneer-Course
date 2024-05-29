package vn.aptech.pixelpioneercourse.dto;

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

    private List<UserDto> users;
}