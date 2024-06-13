package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import vn.aptech.pixelpioneercourse.entities.Role;

import java.util.List;
import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleName(String roleName);
	Optional<Role> findById(int id);
	
	@Modifying
    @Transactional
    @Query("UPDATE Role r SET r.roleName = :roleName WHERE r.id = :id")
    void updateRoleName(@Param("id") Integer id, @Param("roleName") String roleName);
	
	@Query("SELECT u FROM Role u WHERE u.roleName LIKE %:keyword%")
	List<Role> findByroleName(@Param("keyword") String keyword);
}
