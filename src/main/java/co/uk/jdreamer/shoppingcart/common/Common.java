package co.uk.jdreamer.shoppingcart.common;

import co.uk.jdreamer.shoppingcart.models.Cart;
import co.uk.jdreamer.shoppingcart.models.Category;
import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.repositories.CategoryRepository;
import co.uk.jdreamer.shoppingcart.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class Common {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute
    public void sharedData(Model model, HttpSession session, Principal principal) {

        if (principal != null) {
            model.addAttribute("principal", principal.getName());
        }

        List<Page> commonPages = pageRepository.findAllByOrderBySortingAsc();

        List<Category> commonCategories = categoryRepository.findAllByOrderBySortingAsc();

        boolean cartActive = false;

        if (session.getAttribute("cart") != null) {
            Map<Integer, Cart> cart = (Map<Integer, Cart>) session.getAttribute("cart");

            int size = 0;
            double total = 0;

            for (Cart value : cart.values()) {
                size += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }

            model.addAttribute("commonSize", size);
            model.addAttribute("commonTotal", total);

            cartActive = true;
        }

        model.addAttribute("commonPages", commonPages); // Will send to the UI all the common pages
        model.addAttribute("commonCategories", commonCategories); // Will send to the UI all the common categories
        model.addAttribute("cartActive", cartActive);

    }
}
