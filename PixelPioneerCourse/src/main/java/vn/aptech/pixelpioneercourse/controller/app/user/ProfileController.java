package vn.aptech.pixelpioneercourse.controller.app.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.CourseDto;
import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.dto.UserDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Progress;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.CourseService;
import vn.aptech.pixelpioneercourse.service.ProgressService;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/users")
public class ProfileController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ProgressService progressService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private ResetPasswordController mailer;
	
	@GetMapping("/profile/{id}")
    public String profilePage(@PathVariable("id") Integer id, RedirectAttributes ra, Model model, HttpSession session) {
        try {
            UserDto u = userService.findByID(id);
            Object isUser = session.getAttribute("isUser");
            Object isInstructor = session.getAttribute("isInstructor");
            Object isAdmin = session.getAttribute("isAdmin");

            if (isUser == null && isInstructor == null && isAdmin == null) {
                throw new Exception("User session is not valid.");
            }

            User user = userService.findById(id);
            final Set<CourseDto> courses = new HashSet<>();

            if (user.getRole().getRoleName().equals("ROLE_INSTRUCTOR")) {
                List<Course> instructorCourses = courseService.findByInstructorId(id);
                courses.addAll(instructorCourses.stream().map(course -> mapper.map(course, CourseDto.class)).collect(Collectors.toSet()));
            } else {
                List<Progress> progresses = progressService.findByUserId(id);
                if (progresses != null) {
                    progresses.forEach(progress -> {
                        Course course = progress.getSubLesson().getLesson().getCourse();
                        courses.add(mapper.map(course, CourseDto.class));
                    });
                }
            }

            model.addAttribute("user", u);
            model.addAttribute("courses", courses);
            return "app/user_view/profile";
        } catch (Exception e) {
            ra.addFlashAttribute("ErrorCondition", true);
            ra.addFlashAttribute("ErrorError", "Something is wrong with your profile, contact us for further support.");
            return "redirect:/";
        }
    }
	
	@GetMapping("/profile/edit-my-profile")
	public String editPage(Model model, @SessionAttribute("userId") int userid) {
		try {
			UserCreateDto dto = mapper.map(userService.findById(userid), UserCreateDto.class);
			model.addAttribute("user", dto);
			return "app/user_view/editProfile";
		} catch (Exception e) {
			return "redirect:/app/course";
		}
	}
	
	private String requested_code = null;
	
	@PostMapping("/profile/edit-my-profile/updatedCheck")
	public String edit(@RequestParam("id") String uid, Model model, @ModelAttribute("user") UserCreateDto updatedDto, HttpSession session, RedirectAttributes ra) {
	    try {
	        User pre_u = userService.findById(Integer.parseInt(uid));
	        if (!pre_u.getEmail().equals(updatedDto.getEmail())) {
	            mailer.sendSimpleMessage(updatedDto.getEmail());
	            session.setAttribute("emailChangeCondition", true);
	            session.setAttribute("updatedUser", updatedDto);
	            requested_code = mailer.requestedCode;
	            return "redirect:/app/users/profile/edit-my-profile";
	        }
	        userService.update(updatedDto, Integer.parseInt(uid));
	        User u = userService.findById(Integer.parseInt(uid));
	        session.setAttribute("user", u);
	        return "redirect:/app/users/profile/"+uid;
	    } catch (Exception e) {
	        ra.addAttribute("ErrorCondition", true);
	        ra.addAttribute("ErrorError", e.getMessage());
	        return "redirect:/app/users/profile/edit-my-profile";
	    }
	}

	@PostMapping("/profile/edit-my-profile/updatedCodeCheck")
	public String checkCode(@RequestParam("id") String uid, @RequestParam("codeChecker") String inputCode, HttpSession session, RedirectAttributes ra) {
	    try {
	    	String code = requested_code;
		    if (code.equals(inputCode)) {
		        UserCreateDto updatedDto = (UserCreateDto) session.getAttribute("updatedUser");
		        userService.update(updatedDto, Integer.parseInt(uid));
		        User u = userService.findById(Integer.parseInt(uid));
		        session.setAttribute("user", u);
		        session.removeAttribute("emailChangeCondition");
		        session.removeAttribute("updatedUser");
		        return "redirect:/app/users/profile/"+uid;
		    } else {
		        ra.addAttribute("ErrorCondition", true);
		        ra.addAttribute("ErrorError", "Invalid code");
		        return "redirect:/app/users/profile/edit-my-profile";
		    }
	    } catch (Exception e) {
	    	ra.addAttribute("ErrorCondition", true);
	        ra.addAttribute("ErrorError", e.getMessage());
	        return "redirect:/app/users/profile/edit-my-profile";
	    }
	}

	@PostMapping("/profile/edit-my-profile/cancel")
	public String cancel(HttpSession session) {
		session.removeAttribute("emailChangeCondition");
	    session.removeAttribute("code");
	    session.removeAttribute("updatedUser");
	    requested_code = null;
	    return "redirect:/app/users/profile/edit-my-profile";
	}	
}
