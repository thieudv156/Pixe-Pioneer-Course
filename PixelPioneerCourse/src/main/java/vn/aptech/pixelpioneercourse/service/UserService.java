package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.Authentication;
import vn.aptech.pixelpioneercourse.dto.LoginDto;
import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.dto.UserDto;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    List<User> findAll();
    User findById(int id);
    UserDto findByID(int id);
    User findByEmail(String email);
    User findByUsername(String username);
    String checkLogin(String EmailorUsername, String password);
    UserDetails loadUserByEmailorUsername(String EmailorUsername) throws UsernameNotFoundException;
    boolean create(UserCreateDto u);
    boolean update(UserCreateDto u, int id);
    boolean updateWithRole(User u, int uID);
    void delete(User u);
    Authentication processLogin(LoginDto body);
    Authentication processLogin(String email, String password);
}


