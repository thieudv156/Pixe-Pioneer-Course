package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Discussion;

public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {
}
