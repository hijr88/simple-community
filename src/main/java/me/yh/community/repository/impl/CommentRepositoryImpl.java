package me.yh.community.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.yh.community.Utils;
import me.yh.community.dto.comment.CommentList;
import me.yh.community.dto.comment.QCommentList;
import me.yh.community.entity.QComment;
import me.yh.community.entity.QMember;
import me.yh.community.repository.custom.CommentRepositoryCustom;

import javax.persistence.EntityManager;
import java.util.List;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CommentList> findCommentListById(Long postId, long offset, long limit) {

        QComment c1 = new QComment("c1");
        QComment c2 = new QComment("c2");
        QMember member = QMember.member;

        List<CommentList> list = queryFactory
                .select(new QCommentList(c1.id, c1.content, member.id, member.nickname, member.profileImage, c1.CreateDate, c1.delete, c2.count()))
                .from(c1)
                .join(member).on(c1.writer.eq(member.id))
                .leftJoin(c2).on(c1.id.eq(c2.parent))
                .where(c1.postId.eq(postId).and(c1.parent.eq(0L)))
                .groupBy(c1.id, c1.content, member.id, member.nickname, member.profileImage, c1.CreateDate, c1.delete)
                .offset(offset).limit(limit)
                .fetch();

        list.forEach(c -> {
            c.setProfileImage(Utils.urlEncode(c.getProfileImage()));
        });
        return list;
    }

    @Override
    public List<CommentList> findChildCommentListById(Long commentId, long offset, long limit) {
        QComment c = QComment.comment;
        QMember m = QMember.member;

        List<CommentList> list = queryFactory
                .select(new QCommentList(c.id, c.content, m.id, m.nickname, m.profileImage, c.CreateDate, c.delete))
                .from(c)
                .join(m).on(c.writer.eq(m.id))
                .where(c.parent.eq(commentId))
                .offset(offset).limit(limit)
                .fetch();

        list.forEach(comment -> {
            comment.setProfileImage(Utils.urlEncode(comment.getProfileImage()));
        });
        return list;
    }
}
