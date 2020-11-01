package me.yh.community.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.yh.community.Utils;
import me.yh.community.dto.post.*;
import me.yh.community.entity.QComment;
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
    public List<PostListDto> findListByPageAndPub(PostPage page, Boolean pub) {

        long offset = (page.getCurrent() -1) * 10;

        QPostRecommend recommend = postRecommend;
        QComment comment = QComment.comment;

        return queryFactory
                .select(new QPostListDto(post.id, post.title, post.member.nickname, post.createDate, recommend.countDistinct(), post.hit, post.dept, comment.countDistinct(), post.pub ) )
                .from(post)
                .join(post.member, member)
                .leftJoin(recommend).on(post.id.eq(recommend.postId))
                .leftJoin(comment).on(post.id.eq(comment.postId))
                .where(
                        pubEq(pub),
                        post.delete.eq(false),
                        fieldEq(page)
                )
                .groupBy(post.id, post.title, post.member.nickname, post.createDate, post.hit, post.dept)
                .orderBy(post.groupNo.desc(), post.groupOrder.asc())
                .offset(offset).limit(10)
                .fetch();
    }

    @Override
    public Long countListByPageAndPub(PostPage page, Boolean pub) {
        return queryFactory
                .from(post)
                .join(post.member, member)
                .where(
                        pubEq(pub),
                        post.delete.eq(false),
                        fieldEq(page)
                )
                .fetchCount();
    }

    @Override
    public PostDetailDto findPostDetailByIdAndPub(Long id, Boolean pub) {

        QPostRecommend recommend = postRecommend;
        QPostFile file = postFile;

        PostDetailDto postDetail = queryFactory
                .select(new QPostDetailDto(post.id, post.title, post.content, member.id, member.nickname, member.profileImage, post.createDate, post.hit, recommend.countDistinct(), file.fileName, file.originalFileName, post.pub))
                .from(post)
                .join(post.member, member)
                .leftJoin(post.files, file)
                .leftJoin(recommend).on(post.id.eq(recommend.postId))
                .where(post.id.eq(id).and(pubEq(pub)).and(post.delete.eq(false)))
                .groupBy(post.id, post.title, post.content, member.id, member.nickname, member.profileImage, post.createDate, post.hit, file.fileName, file.originalFileName)
                .fetchOne();

        if (postDetail != null) {
            if (postDetail.getFileName() != null) {
                postDetail.setFileName(Utils.urlEncode(postDetail.getFileName()));
                //postDetail.setOriginalFileName(Utils.urlEncode(postDetail.getOriginalFileName()));
            }
            postDetail.setProfileImage(Utils.urlEncode(postDetail.getProfileImage()));
        }
        return postDetail;
    }

    @Override
    public PostEditDto findPostEditById(Long id) {

        QPostFile file = postFile;

        return queryFactory
                .select(new QPostEditDto(post.title, post.content, post.member.id, file.originalFileName))
                .from(post)
                .leftJoin(post.files, file)
                .where(post.id.eq(id).and(post.pub.eq(true)).and(post.delete.eq(false)))
                .fetchOne();
    }



    private BooleanExpression fieldEq(PostPage page) {
        if (page.getQuery().equals(""))
            return null;
        if (page.getField().equals("title")) {
            return post.title.containsIgnoreCase(page.getQuery());
        } else if (page.getField().equals("writer")) {
            return member.nickname.containsIgnoreCase(page.getQuery());
        }
        return null;
    }

    private BooleanExpression pubEq(Boolean pubCond) {
        if (pubCond == null) {
            return null;
        }
        return post.pub.eq(pubCond);
    }
}
