package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.RoleDto;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDto> findAll();
    List<String> findAllToName();
    Optional<RoleDto> findById(int id);
}
