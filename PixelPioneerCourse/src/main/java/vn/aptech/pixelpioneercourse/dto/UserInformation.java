package vn.aptech.pixelpioneercourse.dto;

import java.util.List;

import lombok.Getter;

import vn.aptech.pixelpioneercourse.entities.User;

@Getter
public class UserInformation {
    private final int id;
    private final String email;
    private final List<String> roles;
    public UserInformation(User user){
        id = user.getId();
        email = user.getEmail();
        roles = user.getAuthorities();
    }
    
}