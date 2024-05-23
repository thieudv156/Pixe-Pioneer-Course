package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int id);
}
