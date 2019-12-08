package co.uk.jdreamer.shoppingcart.controllers;

import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.services.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        List<Page> pages = pageService.findAllByOrderBySortingAsc();
        // Pass the list of pages to index view
        model.addAttribute("pages", pages);

        return "admin/pages/index";
    }

    /*
     *
     * ADD NEW PAGES
     *
     * */

    @GetMapping("/add")
    public String add(@ModelAttribute Page page) {
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            return "admin/pages/add";
        }

        redirectAttributes.addFlashAttribute("message", "Page added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");
        Page slugExists = pageService.findBySlug(slug);

        if (slugExists != null) {
            redirectAttributes.addFlashAttribute("message", "Slug exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page); // this keep the values inserted in the form
        } else {
            page.setSlug(slug);
            page.setSorting(100); // set as the last page

            pageService.savePage(page);
        }
        return "redirect:/admin/pages/add";
    }

    /*
     *
     * EDIT PAGES
     *
     * */

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Page page = pageService.findPageById(id);

        model.addAttribute("page", page);

        return "admin/pages/edit";

    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Page currentPage = pageService.findPageById(page.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", currentPage.getTitle());
            return "admin/pages/edit";
        }

        redirectAttributes.addFlashAttribute("message", "Page edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");
        Page slugExists = pageService.findBySlugAndIdNot(slug, page.getId());

        if (slugExists != null) {
            redirectAttributes.addFlashAttribute("message", "Slug exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page); // this keep the values inserted in the form
        } else {
            page.setSlug(slug);

            pageService.savePage(page);
        }
        return "redirect:/admin/pages/edit/" + page.getId();
    }

    /*
     *
     * DELETE PAGES
     *
     * */

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {

        pageService.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Page deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/pages";

    }

    @PostMapping("/reorder")
    public @ResponseBody
    String reorder(@RequestParam("id[]") int[] id) {

        int count = 1;
        Page page;

        for (int pageId : id) {
            page = pageService.findPageById(pageId);
            page.setSorting(count);
            pageService.savePage(page);
            count++;
        }

        return "ok";
    }

}
