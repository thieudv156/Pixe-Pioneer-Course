package vn.aptech.pixelpioneercourse.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.aptech.pixelpioneercourse.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))")
	List<Category> findByNameIgnoreCase(@Param("query") String query);
	Category findByName(String name);
}
