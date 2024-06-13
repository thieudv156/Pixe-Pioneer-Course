package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.RoleCreateDto;
import vn.aptech.pixelpioneercourse.dto.RoleDto;
import vn.aptech.pixelpioneercourse.entities.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDto> findAll();
    List<String> findAllToName();
    Optional<RoleDto> findById(int id);
    Optional<RoleDto> findByRoleName(String roleName);
    List<Role> searchByQuery(String query);
    boolean create(RoleCreateDto role);
    void update(String uid, RoleCreateDto role);
    void delete(String rID);
}
