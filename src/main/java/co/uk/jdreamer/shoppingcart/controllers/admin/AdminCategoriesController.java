package co.uk.jdreamer.shoppingcart.controllers.admin;

import co.uk.jdreamer.shoppingcart.models.Category;
import co.uk.jdreamer.shoppingcart.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        List<Category> categories = categoryRepository.findAllByOrderBySortingAsc();
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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Category category = categoryRepository.getOne(id);

        model.addAttribute("category", category);

        return "admin/categories/edit";

    }

    @PostMapping("/edit")
    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Category currentCategory = categoryRepository.getOne(category.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryName", currentCategory.getName());
            return "admin/categories/edit";
        }

        redirectAttributes.addFlashAttribute("message", "Category edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = category.getName().toLowerCase().replace(" ", "-");
        Category categoryExists = categoryRepository.findByName(category.getName());

        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else {
            category.setSlug(slug);
            categoryRepository.save(category);
        }

        return "redirect:/admin/categories/edit/" + category.getId();

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {

        categoryRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Category deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/categories";

    }

    @PostMapping("/reorder")
    public @ResponseBody
    String reorder(@RequestParam("id[]") int[] id) {

        int count = 1;
        Category category;

        for (int categoryId : id) {
            category = categoryRepository.getOne(categoryId);
            category.setSorting(count);
            categoryRepository.save(category);
            count++;
        }

        return "ok";
    }
}
