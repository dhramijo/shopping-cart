package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Category;
import co.uk.jdreamer.shoppingcart.models.Product;
import co.uk.jdreamer.shoppingcart.repositories.CategoryRepository;
import co.uk.jdreamer.shoppingcart.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{slug}")
    public String category(@PathVariable String slug, Model model, @RequestParam(value = "page", required = false) Integer pageNumber) {

        // Pagination
        int page = (pageNumber != null) ? pageNumber : 0; // Default page number is 0
        int sizePerPage = 6; // Number products per page
        Pageable pageable = PageRequest.of(page, sizePerPage);
        long count = 0; // Number of total product's row

        if (slug.equals("all")) {

            Page<Product> products = productRepository.findAll(pageable);
            count = productRepository.count();
            model.addAttribute("products", products);

        } else {
            Category category = categoryRepository.findBySlug(slug);

            if (category == null) {
                return "redirect:/";
            }

            int categoryId = category.getId();
            String categoryName = category.getName();
            List<Product> products = productRepository.findAllByCategoryId(categoryId, pageable);

            count = productRepository.countByCategoryId(categoryId);

            model.addAttribute("products", products);
            model.addAttribute("categoryName", categoryName);
        }

        double pageCount = Math.ceil((double) count / (double) sizePerPage); // Number of pages needed for the pagination

        // Pass the pagination info to the view
        model.addAttribute("page", page);
        model.addAttribute("sizePerPage", sizePerPage);
        model.addAttribute("count", count);
        model.addAttribute("pageCount", (int) pageCount);

        return "products";
    }
}
