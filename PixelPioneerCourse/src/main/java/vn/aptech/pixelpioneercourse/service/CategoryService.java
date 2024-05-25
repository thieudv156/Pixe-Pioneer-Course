package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();
    public Category findById(int id);
}
