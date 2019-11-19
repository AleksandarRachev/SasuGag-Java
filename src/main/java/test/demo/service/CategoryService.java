package test.demo.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import test.demo.dto.CategoryRequest;
import test.demo.dto.CategoryResponse;
import test.demo.entity.Category;
import test.demo.exception.ElementExistsException;
import test.demo.exception.ElementMissingException;
import test.demo.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CategoryResponse> getAll(){
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .collect(Collectors.toList());
    }

    Category getCategory(String name){
        return categoryRepository.findByName(name).orElseThrow(() -> new ElementMissingException("Invalid category"));
    }

    public CategoryResponse addCategory(CategoryRequest categoryRequest) {

        Optional<Category> validateCategory = categoryRepository.findByName(categoryRequest.getName());
        if (validateCategory.isPresent()) {
            throw new ElementExistsException("Category with that name already exists");
        }
        Category category = categoryRepository.save(new Category(categoryRequest.getName()));
        return modelMapper.map(category, CategoryResponse.class);
    }
}
