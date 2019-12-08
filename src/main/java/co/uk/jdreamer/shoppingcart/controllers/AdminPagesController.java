package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.services.PageService;
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

    @GetMapping("/add")
    public String add(@ModelAttribute Page page) {

        return "admin/pages/add";

    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()){
            return "admin/pages/add";
        }

        redirectAttributes.addFlashAttribute("message","Page added");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ","-") : page.getSlug().toLowerCase().replace(" ","-");
        Page slugExists = pageService.findBySlug(slug);

        if (slugExists != null) {
            redirectAttributes.addFlashAttribute("message","Slug exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("page",page); // this keep the values inserted in the form
        } else {
            page.setSlug(slug);
            page.setSorting(100); // set as the last page

            pageService.savePage(page);
        }
        return "redirect:/admin/pages/add";

    }
}
