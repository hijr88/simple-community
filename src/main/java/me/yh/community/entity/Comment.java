package me.yh.community.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@SequenceGenerator(
        name="comment_seq_gen",
        sequenceName="comment_seq",
        initialValue=1,
        allocationSize=1
)
@Entity
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq_gen")
    @Column(name = "Comment_id")
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(name = "member_id", length = 50, nullable = false)
    private String writer;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime CreateDate;

    @ColumnDefault("0")
    private Long parent;

    @ColumnDefault("'0'")
    private Boolean delete;

    public Comment(Long postId, String content, String writer, Long parent) {
        this.postId = postId;
        this.content = content;
        this.writer = writer;
        this.parent = parent;
    }
}
