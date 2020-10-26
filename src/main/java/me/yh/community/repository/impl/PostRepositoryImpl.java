package me.yh.community.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.yh.community.Utils;
import me.yh.community.dto.post.PostDetailDto;
import me.yh.community.dto.post.PostListDto;
import me.yh.community.dto.post.QPostDetailDto;
import me.yh.community.dto.post.QPostListDto;
import me.yh.community.entity.QPostFile;
import me.yh.community.entity.QPostRecommend;
import me.yh.community.repository.custom.PostRepositoryCustom;

import javax.persistence.EntityManager;
import java.util.List;

import static me.yh.community.entity.QMember.member;
import static me.yh.community.entity.QPost.post;
import static me.yh.community.entity.QPostFile.postFile;
import static me.yh.community.entity.QPostRecommend.postRecommend;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PostListDto> findPostList() {

        QPostRecommend recommend = postRecommend;

        return queryFactory
                .select(new QPostListDto(post.id, post.title, post.member.nickname, post.createDate, recommend.count(), post.hit, post.dept))
                .from(post)
                .join(post.member, member)
                .leftJoin(recommend).on(post.id.eq(recommend.post.id))
                .groupBy(post.id, post.title, post.member.nickname, post.createDate, post.hit, post.dept)
                .orderBy(post.groupNo.desc(), post.groupOrder.asc())
                .fetch();
    }

    @Override
    public PostDetailDto findPostDetailById(Long id) {

        QPostRecommend recommend = postRecommend;
        QPostFile file = postFile;

        PostDetailDto postDetail = queryFactory
                .select(new QPostDetailDto(post.id, post.title, post.content, member.id, member.nickname, member.profileImage, post.createDate, post.hit, recommend.count(), file.fileName, file.originalFileName))
                .from(post)
                .join(post.member, member)
                .leftJoin(post.files, file)
                .leftJoin(recommend).on(post.id.eq(recommend.post.id))
                .where(post.id.eq(id))
                .groupBy(post.id, post.title, post.content, member.id, member.nickname, member.profileImage, post.createDate, post.hit, file.fileName, file.originalFileName)
                .fetchOne();

        if (postDetail != null) {
            if (postDetail.getFileName() != null) {
                postDetail.setFileName(Utils.urlEncode(postDetail.getFileName()));
            }
            postDetail.setProfileImage(Utils.urlEncode(postDetail.getProfileImage()));
        }

        return postDetail;
    }
}
