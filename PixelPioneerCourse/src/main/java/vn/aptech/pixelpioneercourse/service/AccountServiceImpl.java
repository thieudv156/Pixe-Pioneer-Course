//package vn.aptech.pixelpioneercourse.service;
//
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import lombok.extern.slf4j.Slf4j;
//import vn.aptech.pixelpioneercourse.dto.Authentication;
//import vn.aptech.pixelpioneercourse.dto.LoginDto;
//import vn.aptech.pixelpioneercourse.dto.UserInformation;
//import vn.aptech.pixelpioneercourse.entities.Account;
//import vn.aptech.pixelpioneercourse.entities.RefreshToken;
//import vn.aptech.pixelpioneercourse.jwt.JWT;
//import vn.aptech.pixelpioneercourse.repository.RefreshTokenRepository;
//import vn.aptech.pixelpioneercourse.repository.UserRepository;
//import vn.aptech.pixelpioneercourse.dto.AccountDto;
//import vn.aptech.pixelpioneercourse.repository.AccountRepository;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class AccountServiceImpl implements AccountService{
//    @Autowired
//    private AccountRepository accountRepository;
//
//    private final PasswordEncoder encoderr;
//
//    public AccountServiceImpl(AccountRepository acct, PasswordEncoder ed) {
//        accountRepository = acct;
//        encoderr = ed;
//    }
//
//    @Autowired
//    private ModelMapper mapper;
//
//    public List<AccountDto> findAll(){
//        List<AccountDto> accounts = accountRepository.findAll().stream().map(account -> {
//            return mapper.map(account, AccountDto.class);
//        }).toList();
//        return accounts;
//    }
//
//    public Optional<AccountDto> findById(int id){
//        return accountRepository.findById(id).map(account -> {
//            return mapper.map(account, AccountDto.class);
//        });
//    }
//
//    public Optional<AccountDto> findByEmail(String email) {
//        return accountRepository.findByEmail(email).map(account -> {
//            return mapper.map(account, AccountDto.class);
//        });
//    }
//
//    public boolean checkLogin(String email, String password) {
//        Optional<AccountDto> acc = findByEmail(email);
//        if (acc.isPresent()) {
//            AccountDto accountDto = acc.get();
//            String hashedPassword = accountDto.getPassword();
//            return encoder.matches(password, hashedPassword);
//        }
//        return false;
//    }
//
//
//    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
//        Account tbAccount = accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid email or password."));
//        return new User(tbAccount.getEmail(), tbAccount.getPassword(), true, true, true, true, Collections.emptyList());
//    }
//
//
//    public void create(AccountDto accountDto){
//        Account account = mapper.map(accountDto, Account.class);
//        accountRepository.save(account);
//    }
//
//    public void update(AccountDto accountDto){
//        Account existedAccount = accountRepository.findById(accountDto.getId()).orElseThrow(() -> new RuntimeException("Account not found!"));
//        existedAccount.setId(accountDto.getId());
//        existedAccount.setEmail(accountDto.getEmail());
//        existedAccount.setPassword(accountDto.getPassword());
//        existedAccount.setFullname(accountDto.getFullname());
//        existedAccount.setPhone(accountDto.getPhone());
//        accountRepository.save(existedAccount);
//    }
//
//    public void delete(AccountDto accountDto){
//        accountRepository.deleteById(accountDto.getId());
//    }
//
//    
//    // =========================================================================================================
//    /*
//     * API SECTION
//     */
//    
//    @Value("jwt.secret")
//    public String TOKEN_SECRET;
//    
//    @Autowired
//    private RefreshTokenRepository refreshTokenRepository;
//    
//    @Autowired
//    private UserRepository userRepository;
//    
//    @Autowired
//    private JWT jwt;
//    
//    @Autowired
//    private PasswordEncoder encoder;
//    
//    public Authentication processLogin(LoginDto body){
//        String email = body.getEmail();
//        String password = body.getPassword();
//
//        vn.aptech.pixelpioneercourse.entities.User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email or password is invalid!\n"));
//        
//        //so sanh password
//        if(!encoder.matches(password, user.getPassword())){
//            throw new UsernameNotFoundException("Email or password is invalid!");
//        }
//         
//        var expiredAt = LocalDateTime.now().plusDays(1);
//        var accessToken = jwt.encode(user.getId(), user.getAuthorities(), expiredAt, TOKEN_SECRET);
//
//        
//        //Tao refreshToken
//        refreshTokenRepository.disableRefreshTokenFromUser(user.getId());
//        RefreshToken refreshToken = new RefreshToken(user, 7);
//        refreshTokenRepository.save(refreshToken);
//        
//        Authentication authentication = new Authentication(new UserInformation(user), accessToken, refreshToken.getCode(), expiredAt);
//        
//        return authentication;
//    }
//    
//    public Authentication processLogin(String email, String password){
//        
//        if (!checkLogin(email, password)) {
//        	throw new UsernameNotFoundException("Email or password is invalid!");
//        }
//
//        vn.aptech.pixelpioneercourse.entities.User user = userRepository.findByEmail(email).orElseThrow();
//        
////        //so sanh password
////        if(!encoder.matches(password, user.getPassword())){
////            throw new UsernameNotFoundException("Email or password is invalid!");
////        }
//         
//        var expiredAt = LocalDateTime.now().plusDays(1);
//        var accessToken = jwt.encode(user.getId(), user.getAuthorities(), expiredAt, TOKEN_SECRET);
//
//        
//        //Tao refreshToken
//        refreshTokenRepository.disableRefreshTokenFromUser(user.getId());
//        RefreshToken refreshToken = new RefreshToken(user, 7);
//        refreshTokenRepository.save(refreshToken);
//        
//        Authentication authentication = new Authentication(new UserInformation(user), accessToken, refreshToken.getCode(), expiredAt);
//        
//        return authentication;
//    }
//}
