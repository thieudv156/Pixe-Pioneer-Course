package vn.aptech.pixelpioneercourse.controller.app.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String index() {
        return "app/index";
    }
}
