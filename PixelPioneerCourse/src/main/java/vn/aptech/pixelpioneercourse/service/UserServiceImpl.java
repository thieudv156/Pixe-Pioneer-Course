package vn.aptech.pixelpioneercourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.UserDto;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository accountRepository;

    private UserDto toDto(User account){
        return new UserDto(
                account.getId(),
                account.getRole(),
                account.getUsername(),
                account.getPassword(),
                account.getFullName(),
                account.getEmail(),
                account.getPhone()
        );
    }

    private User fromDto(UserDto accountDto){
        return new User(
                accountDto.getId(),
                accountDto.getRole(),
                accountDto.getUsername(),
                accountDto.getPassword(),
                accountDto.getFullName(),
                accountDto.getEmail(),
                accountDto.getPhone()
        );
    }

    public List<UserDto> findAll(){
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
