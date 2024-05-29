package vn.aptech.pixelpioneercourse.dto;

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
