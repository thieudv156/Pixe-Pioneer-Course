package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.aptech.pixelpioneercourse.dto.AccountDto;
import vn.aptech.pixelpioneercourse.entities.Account;
import vn.aptech.pixelpioneercourse.repository.AccountRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;

    private final PasswordEncoder encoder;

    public AccountServiceImpl(AccountRepository acct, PasswordEncoder ed) {
        this.accountRepository = acct;
        this.encoder = ed;
    }

    @Autowired
    private ModelMapper mapper;

    public List<AccountDto> findAll(){
        List<AccountDto> accounts = accountRepository.findAll().stream().map(account -> {
            return mapper.map(account, AccountDto.class);
        }).toList();
        return accounts;
    }

    public Optional<AccountDto> findById(int id){
        return accountRepository.findById(id).map(account -> {
            return mapper.map(account, AccountDto.class);
        });
    }

    public Optional<AccountDto> findByEmail(String email) {
        return accountRepository.findByEmail(email).map(account -> {
            return mapper.map(account, AccountDto.class);
        });
    }

    public boolean checkLogin(String email, String password) {
        Optional<AccountDto> acc = findByEmail(email);
        if (acc.isPresent()) {
            AccountDto accountDto = acc.get();
            String hashedPassword = accountDto.getPassword();
            return encoder.matches(password, hashedPassword);
        }
        return false;
    }


    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Account tbAccount = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));
        return new User(tbAccount.getEmail(), tbAccount.getPassword(), true, true, true, true, Collections.emptyList());
    }


    public void create(AccountDto accountDto){
        Account account = mapper.map(accountDto, Account.class);
        accountRepository.save(account);
    }

    public void update(AccountDto accountDto){
        Account existedAccount = accountRepository.findById(accountDto.getId()).orElseThrow(() -> new RuntimeException("Account not found!"));
        existedAccount.setId(accountDto.getId());
        existedAccount.setEmail(accountDto.getEmail());
        existedAccount.setPassword(accountDto.getPassword());
        existedAccount.setFullname(accountDto.getFullname());
        existedAccount.setPhone(accountDto.getPhone());
        accountRepository.save(existedAccount);
    }

    public void delete(AccountDto accountDto){
        accountRepository.deleteById(accountDto.getId());
    }

}
