package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.AccountDto;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface AccountService {
    List<AccountDto> findAll();
    void create(AccountDto accountDto);

    void update(AccountDto accountDto);

    Optional<AccountDto> findById(int id);

    Optional<AccountDto> findByEmail(String email);

    boolean checkLogin(String email, String password);

    UserDetails loadUserByEmail(String email);

    void delete(AccountDto accountDto);
}
