package vn.aptech.pixelpioneercourse.controller.app;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @RequestMapping("")
    public String handleError() {

        return "app/error/500";
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public String handleServletRequestBindingException(ServletRequestBindingException ex) {
        return "redirect:/app/login";
    }
}
