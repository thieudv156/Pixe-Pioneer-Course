package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.CategoryCreateDto;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(int id);
    Category findByName(String name);
    List<Category> findByQuery(String query);
    boolean save(CategoryCreateDto dto);
    boolean update(int id, CategoryCreateDto dto);
    boolean delete(int id);
}
