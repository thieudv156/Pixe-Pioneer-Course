package vn.aptech.pixelpioneercourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.aptech.pixelpioneercourse.entities.Role;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class UserDto {

    @Getter
    private int id;
    
    @JsonIgnoreProperties({"id", "users"})
    @Getter
    private Role role;
    @Getter
    private String username;
    @Getter
    private String password;
    @Getter
    private String email;
    @Getter
    private String fullName;
    @Getter
    private String phone;
}
