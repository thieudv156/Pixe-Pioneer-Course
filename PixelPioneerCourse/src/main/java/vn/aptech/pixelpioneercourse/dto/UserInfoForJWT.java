package vn.aptech.pixelpioneercourse.dto;

import lombok.Getter;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;

@Getter
public class UserInfoForJWT {
    private final int id;
    private final String email;
    private final List<String> roles;
    public UserInfoForJWT(User user){
        id = user.getId();
        email = user.getEmail();
        roles = user.getAuthorities();
    }

}