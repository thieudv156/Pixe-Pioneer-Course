package vn.aptech.pixelpioneercourse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	Optional<Review> findById(Integer id);
	List<Review> findByCourseId(Integer courseId);
	List<Review> findByUserId(Integer userId);
}
