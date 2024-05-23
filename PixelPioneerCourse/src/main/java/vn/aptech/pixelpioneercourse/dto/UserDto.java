package vn.aptech.pixelpioneercourse.dto;

import lombok.Getter;
import vn.aptech.pixelpioneercourse.entities.Role;

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

    public UserDto(int id, Role role, String username, String password, String email, String fullName, String phone) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
    }
}
