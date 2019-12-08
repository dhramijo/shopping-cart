package co.uk.jdreamer.shoppingcart.services;

import co.uk.jdreamer.shoppingcart.models.Page;
import co.uk.jdreamer.shoppingcart.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    @Autowired
    PageRepository pageRepository;

    public List<Page> findAllPages() {
        return pageRepository.findAll();
    }
}
