package me.yh.community.config.support;

import me.yh.community.dto.post.PostPage;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PostPageHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    String field;
    String query;
    long num;

    Set<String> fieldOption = new HashSet<>(Arrays.asList("title", "writer"));

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //파라미터가 BoardPage 타입일 때
        return parameter.getParameterType().equals(PostPage.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

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
            num = 1;
        } else {
            try { // page_ 문자열이 숫자로 변환이 안되거나 1보다 작을 경우 1로 초기화
                num = Long.parseLong(page_);
                if (num < 1) num = 1;
            } catch (NumberFormatException e) {
                num = 1;
            }
        }

        return new PostPage(field, query, num);
    }
}
