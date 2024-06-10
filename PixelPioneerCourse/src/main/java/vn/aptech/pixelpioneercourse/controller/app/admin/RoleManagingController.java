package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.RoleCreateDto;
import vn.aptech.pixelpioneercourse.dto.RoleDto;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.service.RoleService;

@Controller
@RequestMapping("/app/admin/role")
public class RoleManagingController {
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ModelMapper mapper;
	
	@GetMapping
	public String rolePage(Model model, HttpSession session) {
		try {
			if (session.getAttribute("isAdmin") != null) {
				List<RoleDto> listRole = roleService.findAll();
				model.addAttribute("roles", listRole);
				return "app/admin_view/roles/general";
			} else {
				return "redirect:/app/course";
			}
		} catch (Exception e) {
			return "redirect:/logout";
		}
	}
	
	@GetMapping("/create")
	public String createPage() {
		return "app/admin_view/roles/create";
	}
	
	@PostMapping("/create")
	public String createRole(@RequestParam("roleName") String roleName, RedirectAttributes ra) {
		if (roleService.findByRoleName(roleName).isEmpty()) {
			RoleCreateDto r = new RoleCreateDto();
			r.setName(roleName);
			roleService.create(r);
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Role created successfully.");
			return "redirect:/app/admin/role";
		} else {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", "Role have existed, please create another one.");
			return "redirect:/app/admin/role";
		}
	}
	
	@PostMapping("/update")
	public String updatePage(@RequestParam("id") String rID, Model model) {
	    Role r = mapper.map(roleService.findById(Integer.parseInt(rID)).get(), Role.class);
	    model.addAttribute("roleInfo", r);
	    return "app/admin_view/roles/update";
	}
	
	@PostMapping("/update/updateCheck")
	public String updateRole(@RequestParam("roleName") String roleName, @RequestParam("id") String rid ,RedirectAttributes ra) {
		try {
			RoleCreateDto dto = new RoleCreateDto();
			dto.setName(roleName);
			roleService.update(rid, dto);
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Role changed successfully");
			return "redirect:/app/admin/role";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/role";
		}
	}
	
	@PostMapping("/delete")
	public String deleteRole(@RequestParam("id") String roleId, RedirectAttributes ra) {
		try {
			Role r = mapper.map(roleService.findById(Integer.parseInt(roleId)).get(), Role.class);
		    if (r.getUsers() != null && !r.getUsers().isEmpty()) {
		        ra.addFlashAttribute("ErrorCondition", true);
		        ra.addFlashAttribute("ErrorError", "This role has associated users and cannot be deleted.");
		        return "redirect:/app/admin/role";
		    }
			roleService.delete(roleId);
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Role deleted successfully");
			return "redirect:/app/admin/role";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", "Role deleted unsuccessfully");
			return "redirect:/app/admin/role";
		}
	}
}
