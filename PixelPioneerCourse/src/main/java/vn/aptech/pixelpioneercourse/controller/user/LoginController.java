//package vn.aptech.pixelpioneercourse.controller.user;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import jakarta.servlet.http.HttpServletRequest;
//import vn.aptech.pixelpioneercourse.dto.AccountDto;
//import vn.aptech.pixelpioneercourse.service.AccountService;
//
//@Controller
//@RequestMapping("")
//public class LoginController {
//
//    private final AccountService accountService;
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    public LoginController(AccountService accountService, ModelMapper modelMapper) {
//        this.accountService = accountService;
//        this.modelMapper = modelMapper;
//    }
//
//    @GetMapping("/login")
//    public String index(HttpServletRequest request, Model model) {
//        // Check if the user is already authenticated
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
//            // User is already authenticated, redirect them to the course page
//            return "redirect:/course";
//        }
//        // User is not authenticated, show the login page
//        return "guest_view/login";
//    }
//
//
//    @PostMapping("/logintoaccount")
//    public String checkLogin(@RequestParam String email, @RequestParam String password, Model model) {
//        if (accountService.checkLogin(email, password)) {
//            return "redirect:/course";
//        } else {
//            model.addAttribute("loginError", "Invalid email or password. Please try again.");
//            return "guest_view/login";
//        }
//    }
//}




package vn.aptech.pixelpioneercourse.controller.user;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/login")
public class LoginController {

    private final UserService userService;
    
    @Autowired
    private ModelMapper mapper;

    public LoginController(UserService uService) {
        userService = uService;
    }
    
    @GetMapping
    public String loginPage() {
    	return "guest_view/login";
    }

    @PostMapping
    public String checkLogin(@RequestParam("info") String emailorusername, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        String r = userService.checkLogin(emailorusername, password);
        if (r.equals("")) {
            redirectAttributes.addFlashAttribute("loginErrorCondition", true);
            redirectAttributes.addFlashAttribute("loginError", "Invalid username/email or password");
            return "redirect:/app/login";
        } else if (r.equals("ROLE_USER")) {
            return "redirect:/app/course";
        } else if (r.equals("ROLE_ADMIN")) {
            return "redirect:/app/admin";
        } else if (r.equals("ROLE_INSTRUCTOR")) {
            return "redirect:/app/course";
        }
        redirectAttributes.addFlashAttribute("loginErrorCondition", true);
        redirectAttributes.addFlashAttribute("loginError", "Unknown error occurs");
        return "redirect:/app/login";
    }
}
