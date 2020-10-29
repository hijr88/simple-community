package me.yh.community.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@SequenceGenerator(name="seq_gen", sequenceName="gallery_file_seq",
        initialValue=1, allocationSize=1)
@Table(name = "gallery_file")
@Entity
public class GalleryFile {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @Column(name = "gallery_file_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    @Column(length = 1000, nullable = false)
    private String fileName;

    @Column(length = 1000, nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private int fileSize;

    public GalleryFile(Gallery gallery, String fileName, String originalFileName, int fileSize) {
        this.gallery = gallery;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.fileSize = fileSize;
    }
}
