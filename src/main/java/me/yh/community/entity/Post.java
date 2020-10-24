package me.yh.community.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "member")

@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@SequenceGenerator(
        name="post_seq_gen",
        sequenceName="post_seq",
        initialValue=1,
        allocationSize=1
)
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "post_seq_gen")
    @Column(name = "post_id")
    private Long id;

    @Column(length = 80, nullable = false)
    private String title;

    @Column(length = 4000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member member;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @ColumnDefault("0")
    private Long hit;

    @ColumnDefault("0")
    private Long parent;

    @ColumnDefault("0")
    private long groupNo;  // 새 글인 경우 Save 하고 나서 변경해야 함

    @ColumnDefault("0")
    private Integer groupOrder;

    @ColumnDefault("0")
    private Integer dept;

    @ColumnDefault("'1'")
    private Boolean pub;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "post")
    private PostFile file;

    public static Post createNewPost(String title, String content, Member member) {
        Post post = new Post();

        post.title = title;
        post.content = content;
        post.member = member;

        return post;
    }

    public void changeFile(PostFile file) {
        this.file = file;
    }
}