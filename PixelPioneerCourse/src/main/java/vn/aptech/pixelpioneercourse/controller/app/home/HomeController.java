package vn.aptech.pixelpioneercourse.controller.app.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @RequestMapping
    public String index() {
        return "app/index";
    }
}
