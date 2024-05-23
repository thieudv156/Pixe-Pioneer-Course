package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.UserCreateDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserCreateDto> findAll();
    void create(UserCreateDto accountDto);

    void update(UserCreateDto accountDto);

    Optional<UserCreateDto> findById(int id);

    void delete(UserCreateDto accountDto);
}
