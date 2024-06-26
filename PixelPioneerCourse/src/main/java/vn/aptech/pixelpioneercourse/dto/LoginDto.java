package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
	
	@NotBlank
	private String eu; //email or username
	
	@NotBlank
	private String password;
}
