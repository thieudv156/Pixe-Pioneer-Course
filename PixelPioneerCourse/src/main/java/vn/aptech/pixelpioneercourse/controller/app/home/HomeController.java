package vn.aptech.pixelpioneercourse.controller.app.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String index(HttpSession session) {
//    	System.out.println(session.getAttribute("enrollment").toString());
        return "app/index";
    }
}
