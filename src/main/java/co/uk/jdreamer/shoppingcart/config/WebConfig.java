package co.uk.jdreamer.shoppingcart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        // Set the root view
//        registry.addViewController("/").setViewName("home");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/media/**")
                .addResourceLocations("file:/C:/Users/Jonad/JDREAMER/1_SpringBootProjects/shopping-cart/src/main/resources/static/media/");
    }
}
