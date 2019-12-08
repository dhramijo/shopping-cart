package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    PageService pageService;

    @GetMapping
    public String index(Model model) {

        List<Page> pages = pageService.findAllPages();
        // Pass the list of pages to index view
        model.addAttribute("pages", pages);

        return "admin/pages/index";

    }
}
