package me.yh.community.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@SequenceGenerator(name="seq_gen", sequenceName="post_file_seq",
        initialValue=1, allocationSize=1)
@Entity
public class PostFile {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @Column(name = "post_file_id")
    private Long id;

    @ManyToOne
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
