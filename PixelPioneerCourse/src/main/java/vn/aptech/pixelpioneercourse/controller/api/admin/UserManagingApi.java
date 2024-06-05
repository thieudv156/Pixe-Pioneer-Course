package vn.aptech.pixelpioneercourse.controller.api.admin;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

@RestController
@RequestMapping(value = "/api/admin/users")
public class UserManagingApi {
	
	@Autowired
    private UserService service;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping
	public ResponseEntity<List<User>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> userDetails(@PathVariable("id") int id) {
		User u = service.findById(id);
		if (u != null) {
			return ResponseEntity.ok(u);
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Boolean> updateUserDetails(@PathVariable("id") int id, @RequestBody User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		if (service.updateWithRole(user, id)) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
	}
	
	@PutMapping("/deactivate/{id}")
	public ResponseEntity<Boolean> deactivateAccount(@PathVariable("id") int id) {
		User u = service.findById(id);
		if (u != null) {
			u.setActiveStatus(!u.isActiveStatus());
			service.updateWithRole(u, id);
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
	}
	
	@PutMapping("/activate/{id}")
	public ResponseEntity<Boolean> activateAccount(@PathVariable("id") int id) {
		User u = service.findById(id);
		if (u != null) {
			u.setActiveStatus(!u.isActiveStatus());
			service.updateWithRole(u, id);
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
	}
	
	@PostMapping
	public ResponseEntity<Boolean> createAccount(@RequestBody UserCreateDto dto) {
		dto.setPassword(encoder.encode(dto.getPassword()));
		if (service.create(dto)) {
			return ResponseEntity.ok(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
	}
}
