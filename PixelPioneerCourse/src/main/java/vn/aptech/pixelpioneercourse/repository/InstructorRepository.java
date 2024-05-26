package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Instructor;


public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
}
