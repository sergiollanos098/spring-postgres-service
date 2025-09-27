package com.example.shop.controller;

import com.example.shop.model.Category;
import com.example.shop.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Category> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Category one(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }

    @PostMapping
    public Category create(@RequestBody Category c) { return repo.save(c); }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category c) {
        Category cat = repo.findById(id).orElseThrow();
        cat.setTitle(c.getTitle());
        cat.setDescription(c.getDescription());
        return repo.save(cat);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
