package me.yh.community.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.yh.community.dto.member.MemberListDto;
import me.yh.community.dto.member.MemberPage;
import me.yh.community.dto.member.QMemberListDto;
import me.yh.community.dto.post.PostPage;
import me.yh.community.entity.QMember;
import me.yh.community.repository.custom.MemberRepositoryCustom;

import javax.persistence.EntityManager;
import java.util.List;

import static me.yh.community.entity.QMember.member;
import static me.yh.community.entity.QPost.post;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberListDto> findListByPageAndPub(MemberPage page, boolean enable) {
        long offset = (page.getCurrent() -1) * 10;

        QMember member = QMember.member;

        return queryFactory
                .select(new QMemberListDto(member.id, member.nickname, member.createDate, new CaseBuilder()
                                                                .when(member.enable.eq(false)).then("활동중지")
                                                                .when(member.role.eq("ROLE_MASTER")).then("마스터")
                                                                .when(member.role.eq("ROLE_ADMIN")).then("관리자")
                                                                .otherwise("사용자")))
                .from(member)
                .where(fieldEq(page), member.enable.eq(enable))
                .orderBy(new CaseBuilder()
                        .when(member.enable.eq(false)).then(4)
                        .when(member.role.eq("ROLE_MASTER")).then(1)
                        .when(member.role.eq("ROLE_ADMIN")).then(2)
                        .otherwise(3).asc(), member.createDate.desc())
                .offset(offset).limit(10)
                .fetch();
    }

    @Override
    public Long countListByPageAndPub(MemberPage page, boolean enable) {
        QMember member = QMember.member;

        return queryFactory
                .select(member.count())
                .from(member)
                .where(fieldEq(page), member.enable.eq(enable))
                .fetchOne();
    }

    private BooleanExpression fieldEq(MemberPage page) {
        if (page.getQuery().equals(""))
            return null;
        if (page.getField().equals("id")) {
            return member.id.containsIgnoreCase(page.getQuery());
        } else if (page.getField().equals("name")) {
            return member.nickname.containsIgnoreCase(page.getQuery());
        }
        return null;
    }
}
