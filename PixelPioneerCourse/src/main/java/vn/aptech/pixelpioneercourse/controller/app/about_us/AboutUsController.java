package vn.aptech.pixelpioneercourse.controller.app.about_us;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {
    @GetMapping("/about-us")
    public String aboutUsPage() {
        return "app/guest_view/about-us";
    }
}
