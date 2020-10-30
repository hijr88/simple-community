package me.yh.community.config;

import me.yh.community.config.support.GalleryPageHandlerMethodArgumentResolver;
import me.yh.community.config.support.PostPageHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    PostPageHandlerMethodArgumentResolver postPageHandlerMethodArgumentResolver;

    @Autowired
    GalleryPageHandlerMethodArgumentResolver galleryPageHandlerMethodArgumentResolver;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/index","/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(postPageHandlerMethodArgumentResolver);
        resolvers.add(galleryPageHandlerMethodArgumentResolver);
    }

}
