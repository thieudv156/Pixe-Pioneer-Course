package vn.aptech.pixelpioneercourse.controller.api.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.dto.AccountDto;
import vn.aptech.pixelpioneercourse.dto.Authentication;
import vn.aptech.pixelpioneercourse.dto.LoginDto;
import vn.aptech.pixelpioneercourse.service.AccountService;

@RestController
@RequestMapping(value = "/api")
public class LoginApi {
    @Autowired
    private AccountService service;
    
    @PostMapping(value = "/login")
    public ResponseEntity<Authentication> login(@RequestBody LoginDto body){
        var session = service.processLogin(body);
        return ResponseEntity.ok(session);
    }
    
    @GetMapping(value = "/accounts")
    public ResponseEntity<List<AccountDto>> findAllAccount(){
        List<AccountDto> result = service.findAll();
        return ResponseEntity.ok(result);
    }
}