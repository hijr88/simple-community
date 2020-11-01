package me.yh.community.config.support;

import me.yh.community.dto.post.PostListDto;
import me.yh.community.dto.post.PostPage;
import me.yh.community.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PostPageHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final PostRepository postRepository;

    public PostPageHandlerMethodArgumentResolver(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //파라미터가 BoardPage 타입일 때
        return parameter.getParameterType().equals(PostPage.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String field;
        String query;
        long page;  //현재 페이지 번호
        List<PostListDto> list;
        long totalCount;

        Set<String> fieldOption = new HashSet<>(Arrays.asList("title", "writer"));

        field = webRequest.getParameter("f");
        if (field == null) {
            field = "title";
        }
        else if (!fieldOption.contains(field)) { //필드명이 유효하지 않은경우 title 로 초기화
            field = "title";
        }

        query = webRequest.getParameter("q");
        if (query == null) {
            query = "";
        }

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

        PostPage postPage = new PostPage(field,query,page);

        if (requestURI.contains("/admin")) {
            list = postRepository.findListByPageAndPub(postPage, null);
            totalCount = postRepository.countListByPageAndPub(postPage, null);
        } else {
            list = postRepository.findListByPageAndPub(postPage, true);
            totalCount = postRepository.countListByPageAndPub(postPage, true);
        }
        long pageMaxNum = (long) Math.ceil((totalCount / 10.0));
        pageMaxNum = (pageMaxNum == 0) ? 1 : pageMaxNum;

        return new PostPage(field, query, page, list, totalCount, pageMaxNum);
    }
}
