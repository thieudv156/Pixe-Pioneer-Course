package vn.aptech.pixelpioneercourse.controller.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.service.UserService;

@RestController
@RequestMapping(value = "/api")
public class RegisterApi {

	@Autowired
	private UserService userService;
	
	@PostMapping(value = "/register")
	public ResponseEntity<String> register(@RequestBody UserCreateDto dto){
	    boolean isCreated = userService.create(dto);
	    if (isCreated) {
	        return ResponseEntity.ok("Account Created Successfully");
	    } else {
	        return ResponseEntity.badRequest().body("Registration failed due to an unknown error.");
	    }
	}
}
