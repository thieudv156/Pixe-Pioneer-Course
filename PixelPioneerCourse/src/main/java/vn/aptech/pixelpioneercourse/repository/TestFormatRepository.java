package vn.aptech.pixelpioneercourse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.TestFormat;

public interface TestFormatRepository extends JpaRepository<TestFormat, Integer> {
	List<TestFormat> findByCourseId(Integer courseId);
}
