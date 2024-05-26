package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.AccountDto;
import vn.aptech.pixelpioneercourse.entities.Account;
import vn.aptech.pixelpioneercourse.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;

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
        return (acc.isPresent() && acc.get().getEmail().equals(email) && acc.get().getPassword().equals(password));
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
