package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
@Repository
public interface SubLessonRepository extends JpaRepository<SubLesson, Integer> {
}
