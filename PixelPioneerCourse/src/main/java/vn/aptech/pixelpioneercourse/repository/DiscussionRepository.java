package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.aptech.pixelpioneercourse.entities.Discussion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {
    List<Discussion> findDiscussionBySubLessonId(Integer id);
    Optional<Discussion> findByCreatedAt(LocalDateTime date);
    
    @Query("SELECT d FROM Discussion d " +
    	       "JOIN d.subLesson sl " +
    	       "JOIN sl.lesson l " +
    	       "JOIN l.course c " +
    	       "JOIN d.user u " +
    	       "WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
    	       "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%'))")
    	List<Discussion> findByCourseTitleOrUserFullName(@Param("query") String query);
}
