package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PagesController {

    @Autowired
    private PageRepository pageRepository;

    @GetMapping
    public String home(Model model) {

        Page page = pageRepository.findBySlug("home");
        model.addAttribute("page", page);

        return "page";
    }

    @GetMapping("/{slug}")
    public String page(Model model, @PathVariable String slug) {

        Page page = pageRepository.findBySlug(slug);

        // If there is no page then redirect to Homepage
        if (page == null) {
            return "redirect:/";
        }
        model.addAttribute("page", page);

        return "page";
    }
}
