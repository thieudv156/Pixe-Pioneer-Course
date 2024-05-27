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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.service.AccountService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AccountService accountService;

    @Autowired
    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/loginAccess")
    public ResponseEntity<String> checkLogin(@RequestParam String email, @RequestParam String password) {
        if (accountService.checkLogin(email, password)) {
            System.out.println("LOGIN SUCCESSFUL FOR " + email);
            return ResponseEntity.ok().body("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body("Login failed");
        }
    }
}
