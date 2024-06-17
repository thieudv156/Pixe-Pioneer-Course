package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.aptech.pixelpioneercourse.dto.UserInformation;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String EmailorUsername);

    Optional<User> findById(int id);
    
    Optional<User> findByUsername(String EmailorUsername);
    
    Optional<User> findByPassword(String password);
    
    Optional<User> findByPhone(String phone);
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR u.fullName LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
    @Query("SELECT u FROM User u WHERE u.role.roleName = 'ROLE_INSTRUCTOR'")
    List<UserInformation> findAllInstructor();
}
