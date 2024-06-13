package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.RoleService;
import vn.aptech.pixelpioneercourse.service.UserService;

@RestController
@RequestMapping
public class RestControllerforRealTimeData {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@GetMapping("/app/admin/users/search")
    public List<User> searchUsers(@RequestParam("query") String query, Model model) {
		List<User> uList = userService.searchByQuery(query);
        return uList;
    }
	
	@GetMapping("/app/admin/role/search")
	public List<Role> searchRoles(@RequestParam("query") String query) {
		List<Role> rList = roleService.searchByQuery(query);
		return rList;
	}
}
