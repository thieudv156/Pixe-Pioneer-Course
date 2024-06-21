package vn.aptech.pixelpioneercourse.controller.api.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.dto.Authentication;
import vn.aptech.pixelpioneercourse.dto.LoginDto;
import vn.aptech.pixelpioneercourse.dto.UserInformation;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

@RestController
@RequestMapping(value = "/api")
public class LoginApi {
    @Autowired
    private UserService service;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDto body){
        var session = service.processLogin(body);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account does not exist.");
        }
        return ResponseEntity.ok(session.getUserInformation());
    }

    @GetMapping(value = "/accounts")
    public ResponseEntity<List<User>> findAllAccount(){
        List<User> result = service.findAll();
        return ResponseEntity.ok(result);
    }
}