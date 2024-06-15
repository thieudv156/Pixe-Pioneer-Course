package vn.aptech.pixelpioneercourse.dto;

import java.util.List;

import lombok.Getter;

import vn.aptech.pixelpioneercourse.entities.User;

@Getter
public class UserInformation {
    private final int id;
    private final String fullName;
    private final String email;
    private final String username;
    private final List<String> roles;
    public UserInformation(User user){
        id = user.getId();
        fullName = user.getFullName();
        email = user.getEmail();
        username = user.getUsername();
        roles = user.getAuthorities();
    }
    
}