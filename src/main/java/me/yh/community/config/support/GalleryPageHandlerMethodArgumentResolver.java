package me.yh.community.config.support;

import me.yh.community.dto.gallery.GalleryListDto;
import me.yh.community.dto.gallery.GalleryPage;
import me.yh.community.repository.GalleryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class GalleryPageHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final GalleryRepository galleryRepository;

    public GalleryPageHandlerMethodArgumentResolver(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(GalleryPage.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        long page; //페이지 번호
        List<GalleryListDto> list;
        long totalCount;

        String page_ = webRequest.getParameter("p");
        if (page_ == null) {
            page = 1;
        } else {
            try { // page_ 문자열이 숫자로 변환이 안되거나 1보다 작을 경우 1로 초기화
                page = Long.parseLong(page_);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String requestURI = request.getRequestURI();
        int question =  requestURI.indexOf("?");
        if (question != -1)
            requestURI = requestURI.substring(0, question);
        log.info("접속한 URI : " + requestURI);

        if (requestURI.contains("/admin")) {
            list = galleryRepository.findListByPage(page);
            totalCount = galleryRepository.count();
        } else {
            list = galleryRepository.findListByPageAndPub(page, true);
            totalCount = galleryRepository.countListByPub(true);
        }
        long pageMaxNum = (long) Math.ceil((totalCount / 40.0));
        pageMaxNum = (pageMaxNum == 0) ? 1 : pageMaxNum;

        return new GalleryPage(page, list, totalCount, pageMaxNum);
    }
}
