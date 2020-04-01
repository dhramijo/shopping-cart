package co.uk.jdreamer.shoppingcart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${media.resource.location}")
    private String resourceLocation;
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        // Set the root view
//        registry.addViewController("/").setViewName("home");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/media/**")
                .addResourceLocations(resourceLocation);
    }
}
