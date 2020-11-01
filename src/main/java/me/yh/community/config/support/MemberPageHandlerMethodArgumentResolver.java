package me.yh.community.config.support;

import me.yh.community.dto.member.MemberListDto;
import me.yh.community.dto.member.MemberPage;
import me.yh.community.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MemberPageHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    public MemberPageHandlerMethodArgumentResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //파라미터가 BoardPage 타입일 때
        return parameter.getParameterType().equals(MemberPage.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String field;
        String query;
        long page;  //현재 페이지 번호
        List<MemberListDto> list;
        long totalCount;

        Set<String> fieldOption = new HashSet<>(Arrays.asList("id", "name"));

        field = webRequest.getParameter("f");
        if (field == null) {
            field = "id";
        }
        else if (!fieldOption.contains(field)) { //필드명이 유효하지 않은경우 id 로 초기화
            field = "id";
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

        MemberPage memberPage = new MemberPage(field, query, page);

        list = memberRepository.findListByPageAndPub(memberPage, true);
        totalCount = memberRepository.countListByPageAndPub(memberPage, true);

        long pageMaxNum = (long) Math.ceil((totalCount / 10.0));
        pageMaxNum = (pageMaxNum == 0) ? 1 : pageMaxNum;

        return new MemberPage(field, query, page, list, totalCount, pageMaxNum);
    }
}
