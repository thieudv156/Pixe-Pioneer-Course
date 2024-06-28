package vn.aptech.pixelpioneercourse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.aptech.pixelpioneercourse.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	Optional<Review> findById(Integer id);
	List<Review> findByCourseId(Integer courseId);
	List<Review> findByUserId(Integer userId);

	@Query("SELECT r FROM Review r JOIN r.course c JOIN r.user u WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Review> findByCourseTitleOrUserFullName(@Param("query") String query);
}
