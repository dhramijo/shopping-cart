package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Category;
import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.repositories.CategoryRepository;
import co.uk.jdreamer.shoppingcart.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model) {

        List<Category> categories = categoryRepository.findAll();
        // Pass the list of pages to index view
        model.addAttribute("categories", categories);

        return "admin/categories/index";
    }

    @GetMapping("/add")
    public String add(Category category) {
        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/categories/add";
        }

        redirectAttributes.addFlashAttribute("message", "Category added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category categoryExists = categoryRepository.findByName(category.getName());

        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("categoryInfo", category); // this keep the values inserted in the form
        } else {
            category.setSlug(slug);
            category.setSorting(100); // set as the last page

            categoryRepository.save(category);
        }

        return "redirect:/admin/categories/add";
    }
}
