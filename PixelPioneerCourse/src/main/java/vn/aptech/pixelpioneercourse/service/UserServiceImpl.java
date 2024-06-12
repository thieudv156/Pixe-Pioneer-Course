package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.exceptions.TemplateInputException;

import net.sf.jsqlparser.util.validation.metadata.DatabaseException;
import vn.aptech.pixelpioneercourse.entities.Provider;
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
import java.util.Random;

@Service
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
    
    public List<User> searchByQuery(String query) {
        return userRepository.searchByKeyword(query);
    }
    
    public boolean checkPhone(String phone) {
    	try {
    		userRepository.findByPhone(phone).get();
    		return false;
    	} catch (Exception e) {
    		return true;
    	}
    }

    public User checkLogin(String EmailorUsername, String password) {
        User acc = null;
        try {
        	acc = findByEmail(EmailorUsername);
        	String hashedPassword = acc.getPassword();
            if(encoder.matches(password, hashedPassword)) {
            	return acc;
            }
        } catch (Exception e) {
        	try {
        		acc = findByUsername(EmailorUsername);
        		if (acc != null) {
        			String hashedPassword = acc.getPassword();
                    if(encoder.matches(password, hashedPassword)) {
                    	return acc;
                    }
        		}
        	} catch (Exception e2) {
        		return null;
        	}
        }
        return null;
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
            if (u.getUsername().equals(findByUsername(u.getUsername()).getUsername())) return false;
            
            if (u.getEmail().equals(findByEmail(u.getEmail()).getEmail())) return false;
            
            if (u.getPhone().equals(findByUsername(u.getUsername()).getPhone())) return false;
            
            if (u.getPhone().equals(findByEmail(u.getEmail()).getPhone())) return false;
            
            if (u.getUsername().contains(" ")) {
            	throw new Exception("Username must not contain spaces");
            }
    		if (!u.getEmail().contains("@")) {
    			throw new Exception("Email must contain @");
    		}
        	try {
            	User user = mapper.map(u, User.class);
                user.setPassword(encoder.encode(user.getPassword()));
                user.setActiveStatus(true);
                user.setCreatedAt(LocalDate.now());
                user.setProvider(Provider.LOCAL);
                List<RoleDto> listRole = rService.findAll();
                user.setRole(null); //incase there is no "ROLE_USER" in db;
                for (RoleDto role : listRole) {
                    if (role.getRoleName().equals("ROLE_USER")) user.setRole(convertToRoleFromDto(role));
                }
                res = userRepository.save(user);
            } catch (Exception e2) {
            	throw new DatabaseException("Register fails, contact us for further support");
            }
        }
        catch (Exception e) {
        	try {
        		if (u.getUsername().contains(" ")) {
                	throw new Exception("Username must not contain spaces");
                }
        		if (!u.getEmail().contains("@")) {
        			throw new Exception("Email must contain @");
        		}
            	try {
                	User user = mapper.map(u, User.class);
                    user.setPassword(encoder.encode(user.getPassword()));
                    user.setActiveStatus(true);
                    user.setCreatedAt(LocalDate.now());
                    user.setProvider(Provider.LOCAL);
                    List<RoleDto> listRole = rService.findAll();
                    user.setRole(null); //incase there is no "ROLE_USER" in db;
                    for (RoleDto role : listRole) {
                        if (role.getRoleName().equals("ROLE_USER")) user.setRole(convertToRoleFromDto(role));
                    }
                    res = userRepository.save(user);
                } catch (Exception e2) {
                	throw new DatabaseException("Register fails, contact us for further support");
                }
        	} catch (Exception e1) {
        		throw new TemplateInputException(e1.getMessage());
        	}
        }
        return res != null;
    }
    
    public boolean create(User user) {
    	try {
    		if (user.getUsername().contains(" ")) {
            	throw new Exception("Username must not contain spaces");
            }
    		if (!user.getEmail().contains("@")) {
    			throw new Exception("Email must contain @");
    		}
        	try {
                user.setPassword(encoder.encode(user.getPassword()));
                user.setActiveStatus(true);
                user.setCreatedAt(LocalDate.now());
                user.setProvider(Provider.LOCAL);
                List<RoleDto> listRole = rService.findAll();
                for (RoleDto role : listRole) {
                    if (role.getRoleName().equals("ROLE_USER")) user.setRole(convertToRoleFromDto(role));
                }
                return userRepository.save(user) != null;
            } catch (Exception e2) {
            	throw new DatabaseException("Register fails, contact us for further support");
            }
    	} catch (Exception e1) {
    		throw new TemplateInputException(e1.getMessage());
    	}
    }

    public boolean update(UserCreateDto u, int uID){
    	User acc = userRepository.findById(uID).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        acc.setUsername(u.getUsername());
        acc.setEmail(u.getEmail());
        if (u.getPassword().startsWith("$2a$")) {
        	acc.setPassword(u.getPassword());
        } else {
        	acc.setPassword(encoder.encode(u.getPassword()));
        }
        acc.setFullName(u.getFullName());
        acc.setPhone(u.getPhone());
        userRepository.save(acc);
        return true;
    }
    
    public boolean updateWithRole(User u, int uID){
    	User acc = userRepository.findById(uID).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        acc.setUsername(u.getUsername());
        acc.setEmail(u.getEmail());
        acc.setPassword(u.getPassword());
        acc.setFullName(u.getFullName());
        acc.setPhone(u.getPhone());
        acc.setRole(u.getRole());
        userRepository.save(acc);
        return true;
    }

    public void delete(User u){
        userRepository.deleteById(u.getId());;
    }
    
    public String codeGeneratorForEmailVerification() {
    	Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10)); // Appends a random digit (0-9)
        }
        return code.toString();
    }

    public boolean checkCode(String codeGenerated, String inputCode) {
    	if (codeGenerated.equals(inputCode)) {
			return true;
		} 
    	return false;
    }
    
    public User findUserByPassword(String password) {
    	return userRepository.findByPassword(encoder.encode(password))
                .map(account -> mapper.map(account, User.class))
                .orElseThrow(() -> new UsernameNotFoundException("You have entered an old password, please change it."));
    }
    
    public boolean updatePassword(UserCreateDto u){
    	User acc = userRepository.findByEmail(u.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        acc.setUsername(u.getUsername());
        acc.setEmail(u.getEmail());
        acc.setPassword(u.getPassword());
        acc.setFullName(u.getFullName());
        acc.setPhone(u.getPhone());
        userRepository.save(acc);
        return true;
    }
    
    public void passwordChanger(String email, String password) throws Exception {
    	try {
    		User u = findByEmail(email);
        	UserCreateDto user = mapper.map(u, UserCreateDto.class);
        	if (user == null) {
        		throw new Exception("User does not exist");
        	} else {
        		user.setEmail(email);
        		user.setPassword(encoder.encode(password));
//        		userRepository.save(mapper.map(user, User.class));
        		updatePassword(user);
        	}
    	} catch (Exception e1) {
    		throw new Exception(e1.getMessage());
    	}
    }
    
    public void processOAuthPostLogin(String username) {
        User existUser = userRepository.findByUsername(username).get();
         
        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setCreatedAt(LocalDate.now());
            newUser.setProvider(Provider.GOOGLE);
            newUser.setActiveStatus(true);          
             
            userRepository.save(newUser);        
        }
         
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

        if (checkLogin(email, password) == null) {
            throw new UsernameNotFoundException("Email or password is invalid!");
        }

        User user = userRepository.findByEmail(email).orElseThrow();

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

