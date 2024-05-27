package vn.aptech.pixelpioneercourse.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.service.AccountService;

@RestController
public class LoginController {

    private final AccountService accountService;

    @Autowired
    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "login")
    public ResponseEntity<String> checkLogin(@RequestParam String email, @RequestParam String password) {
        if (accountService.checkLogin(email, password)) {
            System.out.println("LOGIN SUCCESSFUL FOR " + email);
            return ResponseEntity.ok().body("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Login failed");
        }
    }
}
