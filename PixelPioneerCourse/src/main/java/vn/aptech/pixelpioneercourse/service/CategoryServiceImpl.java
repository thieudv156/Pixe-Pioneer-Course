package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.CategoryCreateDto;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    final private CategoryRepository categoryRepository;
    final private ModelMapper mapper;

    private CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper){
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public Category toCategory(CategoryCreateDto dto){return mapper.map(dto,Category.class);}

    public List<Category> findAll(){
        try{
            return categoryRepository.findAll();
        }
        catch (Exception e){
            throw new RuntimeException("List of Category is null");
        }
    }

    public Category findById(int id){
        try {
            return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found!"));
        }
        catch (Exception e){
            throw new RuntimeException("Category is null");
        }
    }

}
