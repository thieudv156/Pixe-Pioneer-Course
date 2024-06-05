package vn.aptech.pixelpioneercourse.controller.api.users.profile;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.dto.UserDto;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

@RestController
@RequestMapping(value = "/api/profile")
public class ProfileApi {
	
	@Autowired
    private UserService service;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserByID(@PathVariable("id") Integer id) {
	    UserDto result = service.findByID(id);
	    result.setPassword(""); //enhance security
	    return ResponseEntity.ok(result);
	}
	
	@GetMapping("/changePassword/{id}")
	public ResponseEntity<Boolean> accessChangePasswordPage(@PathVariable("id") int id) {
		if (service.findById(id).isActiveStatus()) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
	}
	
	@PutMapping("/changePassword/{id}/{nPassword}")
	public ResponseEntity<Boolean> changePassword(@PathVariable("nPassword") String nPassword, @RequestBody UserCreateDto user, @PathVariable("id") int id) {
		try {
			User u = service.findById(id);
			user = mapper.map(u, UserCreateDto.class);
			if (user != null) {
				user.setPassword(encoder.encode(nPassword));
				if (service.update(user, id)) {
					return ResponseEntity.ok(true);
				}
				return ResponseEntity.badRequest().body(false);
			} else {
				throw new UsernameNotFoundException("Cannot find account");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.badRequest().body(false);
	}
	
	@GetMapping("/changeInformation/{id}")
	public ResponseEntity<Boolean> accessChangeInfoPage(@PathVariable("id") int id) {
		if (service.findById(id).isActiveStatus()) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
	}
	
	@PutMapping("/changeInformation/{id}")
	public ResponseEntity<String> changeInformation(@RequestBody UserCreateDto dto, @PathVariable("id") int uID) {
		if (service.update(dto, uID)) {
			return ResponseEntity.ok("Update User Info Successfully");
		}
		return ResponseEntity.badRequest().body("Error: Uknown Error Occurs");
	}
}
