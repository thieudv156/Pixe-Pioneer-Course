package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.CategoryCreateDto;
import vn.aptech.pixelpioneercourse.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(int id);
    boolean save(CategoryCreateDto dto);
    boolean update(int id, CategoryCreateDto dto);
    boolean delete(int id);
}
