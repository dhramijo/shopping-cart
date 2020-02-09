package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Category;
import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.models.Product;
import co.uk.jdreamer.shoppingcart.repositories.CategoryRepository;
import co.uk.jdreamer.shoppingcart.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model) {

        List<Product> products = productRepository.findAll();
        // Pass the list of pages to index view
        model.addAttribute("products", products);

        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model) {

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories",categories);

        return "admin/pages/add";
    }
}
