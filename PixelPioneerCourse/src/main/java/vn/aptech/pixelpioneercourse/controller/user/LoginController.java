package vn.aptech.pixelpioneercourse.controller.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.pixelpioneercourse.service.AccountService;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public String index() {return "/login";}

    @PostMapping("/logintoaccount")
    public String checkLogin(String email, String password) {
        if (accountService.checkLogin(email,password)) {
            return "redirect:/course";
        } else {
            return "redirect:/login";
        }
    }
}
