package co.uk.jdreamer.shoppingcart.common;

import co.uk.jdreamer.shoppingcart.models.Category;
import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.repositories.CategoryRepository;
import co.uk.jdreamer.shoppingcart.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class Common {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute
    public void sharedData(Model model) {

        List<Page> commonPages = pageRepository.findAllByOrderBySortingAsc();

        List<Category> commonCategories = categoryRepository.findAll();

        model.addAttribute("commonPages", commonPages); // Will send to the UI all the common pages
        model.addAttribute("commonCategories", commonCategories); // Will send to the UI all the common categories

    }
}
