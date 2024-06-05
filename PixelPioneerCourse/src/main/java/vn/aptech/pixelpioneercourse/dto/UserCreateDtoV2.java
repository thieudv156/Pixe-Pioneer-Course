package vn.aptech.pixelpioneercourse.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.pixelpioneercourse.Provider;
import vn.aptech.pixelpioneercourse.entities.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDtoV2 {
    private String username;

    private String password;

    private String email;

    private String fullName;

    private boolean activeStatus = true;
    
    private String phone;
    
    private Role role;
    
    private LocalDate createdAt;
    
    private Provider provider;
}
