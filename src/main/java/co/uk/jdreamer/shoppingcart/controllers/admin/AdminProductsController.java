package co.uk.jdreamer.shoppingcart.controllers.admin;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer pageNumber) {

        // Pagination
        int page = (pageNumber != null) ? pageNumber : 0; // Default page number is 0
        int sizePerPage = 4; // Number products per page
        long count = productRepository.count(); // Number of total product's row
        double pageCount = Math.ceil((double) count / (double) sizePerPage); // Number of pages needed for the pagination

        Pageable pageable = PageRequest.of(page, sizePerPage);
        Page<Product> products = productRepository.findAll(pageable);
        // End pagination

        List<Category> categories = categoryRepository.findAll();

        Map<Integer, String> mapCategories = new HashMap<Integer, String>();
        for (Category category : categories) {
            mapCategories.put(category.getId(), category.getName());
        }

        // Pass the list of pages to index view
        model.addAttribute("products", products);
        model.addAttribute("mapCategories", mapCategories);

        // Pass the pagination info to the view
        model.addAttribute("page", page);
        model.addAttribute("sizePerPage", sizePerPage);
        model.addAttribute("count", count);
        model.addAttribute("pageCount", (int) pageCount);

        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model) {

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product, BindingResult bindingResult, MultipartFile file,
                      RedirectAttributes redirectAttributes,
                      Model model) throws IOException {

        List<Category> categories = categoryRepository.findAll();

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            return "admin/products/add";
        }

        boolean isFileOK = false;

        // image file upload operations
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        // check if the file is a jpg or png
        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            isFileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Product added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepository.findBySlug(slug);

        if (!isFileOK) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // this keep the values inserted in the form
            redirectAttributes.addFlashAttribute("product", product);
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // this keep the values inserted in the form
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);
            product.setImage(filename);
            productRepository.save(product);

            // Upload the image
            Files.write(path, bytes);
        }
        return "redirect:/admin/products/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Product product = productRepository.getOne(id);
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "admin/products/edit";

    }

    @PostMapping("/edit")
    public String edit(@Valid Product product, BindingResult bindingResult, MultipartFile file,
                       RedirectAttributes redirectAttributes,
                       Model model) throws IOException {

        Product currentProduct = productRepository.getOne(product.getId());
        List<Category> categories = categoryRepository.findAll();

        if (bindingResult.hasErrors()) {
            model.addAttribute("currentProduct", currentProduct.getName());
            model.addAttribute("categories", categories);
            return "admin/products/edit";
        }

        boolean isFileOK = false;

        // image file upload operations
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);


        if (!file.isEmpty()) {
            // check if the file is a jpg or png
            if (filename.endsWith("jpg") || filename.endsWith("png")) {
                isFileOK = true;
            }
        } else {
            isFileOK = true;
        }

        redirectAttributes.addFlashAttribute("message", "Product edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepository.findBySlugAndIdNot(slug, product.getId());

        if (!isFileOK) {
            redirectAttributes.addFlashAttribute("message", "Image must be a jpg or a png");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // this keep the values inserted in the form
            redirectAttributes.addFlashAttribute("product", product);
        } else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // this keep the values inserted in the form
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);

            // Check if a new image is uploaded
            if (!file.isEmpty()) {
                // Get the old image
                Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
                // Delete the old image
                Files.delete(path2);
                // Add new image
                product.setImage(filename);
                Files.write(path, bytes);

            } else {
                product.setImage(currentProduct.getImage());
            }

            productRepository.save(product);

        }
        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {

        Product product = productRepository.getOne(id);
        // Get the old image
        Path path2 = Paths.get("src/main/resources/static/media/" + product.getImage());
        // Delete the old image
        Files.delete(path2);
        productRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Product deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/products";

    }
}
