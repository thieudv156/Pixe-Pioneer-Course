package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
