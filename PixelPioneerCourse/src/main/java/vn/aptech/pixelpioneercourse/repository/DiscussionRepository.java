package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.pixelpioneercourse.entities.Discussion;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Integer> {
    List<Discussion> findDiscussionBySubLessonId(int id);
}
