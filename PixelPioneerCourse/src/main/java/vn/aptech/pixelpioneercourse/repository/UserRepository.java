package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //@Query("SELECT object FROM from User WHERE o.email=:email")
    //Spring tu sinh ma
    Optional<User> findByEmail(String email);

    Optional<User> findById(int id);
}
