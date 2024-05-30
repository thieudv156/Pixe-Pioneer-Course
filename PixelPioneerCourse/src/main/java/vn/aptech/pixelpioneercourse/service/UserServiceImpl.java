package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.aptech.pixelpioneercourse.dto.Authentication;
import vn.aptech.pixelpioneercourse.dto.LoginDto;
import vn.aptech.pixelpioneercourse.dto.RoleDto;
import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.dto.UserDto;
import vn.aptech.pixelpioneercourse.dto.UserInformation;
import vn.aptech.pixelpioneercourse.entities.RefreshToken;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.jwt.JWT;
import vn.aptech.pixelpioneercourse.repository.RefreshTokenRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final RoleService rService;

    public UserServiceImpl(UserRepository acct, PasswordEncoder ed, RoleService rs) {
        userRepository = acct;
        encoder = ed;
        rService = rs;
    }

    @Autowired
    private ModelMapper mapper;

    public List<User> findAll(){
        List<User> accounts = userRepository.findAll().stream().map(account -> {
            return mapper.map(account, User.class);
        }).toList();
        return accounts;
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .map(account -> mapper.map(account, User.class))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
    
    public UserDto findByID(int id) {
    	return userRepository.findById(id).map(account -> mapper.map(account, UserDto.class)).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(account -> mapper.map(account, User.class))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(account -> mapper.map(account, User.class))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public boolean checkLogin(String EmailorUsername, String password) {
        User acc = findByEmail(EmailorUsername);
        if (acc == null) acc = findByUsername(EmailorUsername);
        if (acc != null) {
            String hashedPassword = acc.getPassword();
            return encoder.matches(password, hashedPassword);
        }
        return false;
    }


    public UserDetails loadUserByEmailorUsername(String EmailorUsername) throws UsernameNotFoundException {
        User tbUser = userRepository.findByEmail(EmailorUsername)
                .orElseGet(() -> userRepository.findByUsername(EmailorUsername)
                        .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password.")));

        if (tbUser == userRepository.findByEmail(EmailorUsername).get()) {
            return new org.springframework.security.core.userdetails.User(tbUser.getEmail(), tbUser.getPassword(), true, true, true, true, Collections.emptyList());
        }
        return new org.springframework.security.core.userdetails.User(tbUser.getUsername(), tbUser.getPassword(), true, true, true, true, Collections.emptyList());

    }

    private Role convertToRoleFromDto(RoleDto dto) {
        return mapper.map(dto, Role.class);
    }


    public boolean create(UserCreateDto u){
        Object res = null;
        try
        {
            if (u.getUsername().equals(findByUsername(u.getUsername()).getUsername()) ||
                    u.getEmail().equals(findByEmail(u.getEmail()).getEmail()) ||
                    u.getPhone().equals(findByUsername(u.getUsername()).getPhone()) ||
                    u.getEmail().equals(findByEmail(u.getEmail()).getPhone())) {
                return false;
            }
        }
        catch (Exception e) {
            User user = mapper.map(u, User.class);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setActiveStatus(true);
            user.setCreatedAt(LocalDate.now());
            List<RoleDto> listRole = rService.findAll();
            for (RoleDto role : listRole) {
                if (role.getRoleName().equals("ROLE_USER")) user.setRole(convertToRoleFromDto(role));
            }
            res = userRepository.save(user);
        }
        return res != null;
    }

    public boolean update(UserCreateDto u, int uID){
    	User acc = userRepository.findById(uID).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        acc.setUsername(u.getUsername());
        acc.setEmail(u.getEmail());
        acc.setPassword(encoder.encode(acc.getPassword()));
        acc.setFullName(u.getFullName());
        acc.setPhone(u.getPhone());
        userRepository.save(acc);
        return true;
    }
    
    public boolean updateWithRole(User u, int uID){
    	User acc = userRepository.findById(uID).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        acc.setUsername(u.getUsername());
        acc.setEmail(u.getEmail());
        acc.setPassword(encoder.encode(acc.getPassword()));
        acc.setFullName(u.getFullName());
        acc.setPhone(u.getPhone());
        userRepository.save(acc);
        return true;
    }

    public void delete(User u){
        userRepository.deleteById(u.getId());;
    }


    // =========================================================================================================
    /*
     * API SECTION
     */

    @Value("jwt.secret")
    public String TOKEN_SECRET;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JWT jwt;


    public Authentication processLogin(LoginDto body){
        String EmailorUsername = body.getEu();
        String password = body.getPassword();

        User user = null;
        Optional<User> userByEmail = userRepository.findByEmail(EmailorUsername);
        if (userByEmail.isPresent()) {
            user = userByEmail.get();
        }

        else {
            Optional<User> userByUsername = userRepository.findByUsername(EmailorUsername);
            if (userByUsername.isPresent()) {
                user = userByUsername.get();
            }
        }

        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new UsernameNotFoundException("Email or password is invalid!");
        }

        var expiredAt = LocalDateTime.now().plusDays(1);
        var accessToken = jwt.encode(user.getId(), user.getAuthorities(), expiredAt, TOKEN_SECRET);

        // Disable any existing refresh tokens for the user
        refreshTokenRepository.disableRefreshTokenFromUser(user.getId());
        RefreshToken refreshToken = new RefreshToken(user, 7);
        refreshTokenRepository.save(refreshToken);

        Authentication authentication = new Authentication(new UserInformation(user), accessToken, refreshToken.getCode(), expiredAt);

        return authentication;
    }

    public Authentication processLogin(String email, String password){

        if (!checkLogin(email, password)) {
            throw new UsernameNotFoundException("Email or password is invalid!");
        }

        vn.aptech.pixelpioneercourse.entities.User user = userRepository.findByEmail(email).orElseThrow();

//        //so sanh password
//        if(!encoder.matches(password, user.getPassword())){
//            throw new UsernameNotFoundException("Email or password is invalid!");
//        }

        var expiredAt = LocalDateTime.now().plusDays(1);
        var accessToken = jwt.encode(user.getId(), user.getAuthorities(), expiredAt, TOKEN_SECRET);


        //Tao refreshToken
        refreshTokenRepository.disableRefreshTokenFromUser(user.getId());
        RefreshToken refreshToken = new RefreshToken(user, 7);
        refreshTokenRepository.save(refreshToken);

        Authentication authentication = new Authentication(new UserInformation(user), accessToken, refreshToken.getCode(), expiredAt);

        return authentication;
    }
}

