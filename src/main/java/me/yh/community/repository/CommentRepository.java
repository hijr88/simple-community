package me.yh.community.repository;

import me.yh.community.entity.Comment;
import me.yh.community.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    long countByPostIdAndParent(Long postId, Long Parent);

    long countByPostId(Long postId);

    long countByParent(Long parent);

    @Transactional
    @Modifying
    @Query("update Comment c set c.delete = true where c.id = :id")
    void updateDeleteByCommentId(@Param("id") long commentId);
}
