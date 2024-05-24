package vn.aptech.pixelpioneercourse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountDto {
    private int id;
    private String email;
    private String password;
    private String fullname;
    private String phone;
}
