package me.yh.community.repository.custom;

import me.yh.community.dto.member.MemberListDto;
import me.yh.community.dto.member.MemberPage;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberListDto> findListByPageAndPub(MemberPage memberPage, boolean enable);

    Long countListByPageAndPub(MemberPage memberPage, boolean enable);
}
