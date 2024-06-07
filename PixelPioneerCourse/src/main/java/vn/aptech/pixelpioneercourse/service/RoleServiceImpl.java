package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.aptech.pixelpioneercourse.dto.RoleCreateDto;
import vn.aptech.pixelpioneercourse.dto.RoleDto;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper mapper;

    public List<RoleDto> findAll()
    {
        return roleRepository.findAll().stream().map(o->mapper.map(o, RoleDto.class)).toList();
    }
    
    public List<String> findAllToName() {
    	return roleRepository.findAll().stream().map(o->mapper.map(o.getRoleName(), String.class)).toList();
    }

    public Optional<RoleDto> findById(int id)
    {
        Optional<Role> result = roleRepository.findById(id);
        return result.map(o->mapper.map(o, RoleDto.class));
    }
    
    public Optional<RoleDto> findByRoleName(String roleName) {
    	Optional<Role> result = roleRepository.findByRoleName(roleName);
    	return result.map(o->mapper.map(o, RoleDto.class));
    }
    
    public boolean create(RoleCreateDto role) {
    	Role r = new Role();
    	r.setRoleName(role.getName());
    	Object a = roleRepository.save(r);
    	return a != null;
    }
    
    public void update(String id, RoleCreateDto role) {
    	roleRepository.updateRoleName(Integer.parseInt(id), role.getName());
    }
    
    public void delete(String rID) {
    	Role r = mapper.map(findById(Integer.parseInt(rID)), Role.class);
    	roleRepository.delete(r);
    }
}
