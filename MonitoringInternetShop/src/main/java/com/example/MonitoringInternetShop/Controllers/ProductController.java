package com.example.MonitoringInternetShop.Controllers;

import com.example.MonitoringInternetShop.Models.Category;
import com.example.MonitoringInternetShop.Models.Product;
import com.example.MonitoringInternetShop.Services.CategoryService;
import com.example.MonitoringInternetShop.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String products(Model model,
                           @RequestParam(name = "category", required = false) String category,
                           @RequestParam(name = "sortBy", required = false) String sortBy) {
        List<Product> products = productService.getFilteredAndSortedProducts(category, sortBy);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
        return "products";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable("id") Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @PostMapping("/{id}/incrementStock")
    public String incrementProductStock(@PathVariable("id") Long id, @RequestParam("incrementAmount") Integer incrementAmount) {
        productService.incrementProductStock(id, incrementAmount);
        return "redirect:/products";
    }


    @PostMapping("/{id}/decrementStock")
    public String decrementProductStock(@PathVariable("id") Long id, @RequestParam("decrementAmount") Integer decrementAmount, Model model) {
        try {
            productService.decrementProductStock(id, decrementAmount);
        } catch (RuntimeException ex) {
            model.addAttribute("alert", ex.getMessage());
            return "products";
        }
        return "redirect:/products";
    }

    @PostMapping("/categories")
    public String createCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return "redirect:/products";
    }

}
