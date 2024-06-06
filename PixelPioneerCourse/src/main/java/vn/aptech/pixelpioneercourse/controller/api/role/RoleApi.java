package vn.aptech.pixelpioneercourse.controller.api.role;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.dto.RoleCreateDto;
import vn.aptech.pixelpioneercourse.dto.RoleDto;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.repository.RoleRepository;
import vn.aptech.pixelpioneercourse.service.RoleService;

@RestController
@RequestMapping(value = "/api/role")
public class RoleApi {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping
	public ResponseEntity<List<RoleDto>> findAll() {
		Optional<List<RoleDto>> resList = Optional.ofNullable(roleService.findAll());
		return resList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RoleDto> findById(@PathVariable("id") String id) {
		Optional<RoleDto> role = roleService.findById(Integer.parseInt(id));
		return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PostMapping("/create")
	public ResponseEntity<Boolean> create(@RequestBody RoleCreateDto roleDto) {
		if (roleService.findByRoleName(roleDto.getName()).isEmpty()) {
			roleService.create(roleDto);
			return ResponseEntity.ok().body(true);
		}
		return ResponseEntity.badRequest().body(false);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Boolean> update(@PathVariable("id") String uid, @RequestBody RoleCreateDto roleDto ) {
		if (roleService.findById(Integer.parseInt(uid)).isPresent()) {
			roleService.update(uid,roleDto);
			return ResponseEntity.ok().body(true);
		}
		return ResponseEntity.badRequest().body(false);
	}
	
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String uid) {
		try {
			roleService.delete(uid);
			return ResponseEntity.ok().body("Delete role successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
