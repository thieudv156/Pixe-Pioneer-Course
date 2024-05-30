package vn.aptech.pixelpioneercourse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RoleDto {
    public int id;
    public String roleName;

    @JsonIgnoreProperties({"password"})
    private List<UserDto> users;
}