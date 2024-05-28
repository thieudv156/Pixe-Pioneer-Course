package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(int id);
}
