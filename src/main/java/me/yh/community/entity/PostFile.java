package me.yh.community.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

@SequenceGenerator(name="seq_gen", sequenceName="post_file_seq",
        initialValue=1, allocationSize=1)
@Entity
public class PostFile {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @Column(name = "post_file_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String fileName;

    private String originalFileName;

    private int fileSize;

    public PostFile(Post post, String fileName, String originalFileName, long fileSize) {
        this.post = post;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.fileSize = (int) fileSize;
    }
}
