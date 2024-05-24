package vn.aptech.pixelpioneercourse.service;

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
    
    private AccountDto toDto(Account account){
        return new AccountDto(
                account.getId(),
                account.getEmail(),
                account.getPassword(),
                account.getFullname(),
                account.getPhone()
        );
    }
    
    private Account fromDto(AccountDto accountDto){
        return new Account(
                accountDto.getId(),
                accountDto.getEmail(),
                accountDto.getPassword(),
                accountDto.getFullname(),
                accountDto.getPhone()
        );
    }
    
    public List<AccountDto> findAll(){
        return accountRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    
    public Optional<AccountDto> findById(int id){
        //Optional<Account> result = accountRepository.findById(id);
        return accountRepository.findById(id).map(this::toDto);
    }
    
    public void create(AccountDto accountDto){
        Account account = fromDto(accountDto);
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
