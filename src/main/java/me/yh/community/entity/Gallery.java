package me.yh.community.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@SequenceGenerator(
        name="gallery_seq_gen",
        sequenceName="gallery_seq",
        initialValue=1,
        allocationSize=1
)
@Entity
public class Gallery {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "gallery_seq_gen")
    @Column(name = "gallery_id")
    private Long id;

    @Column(length = 80, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "writer")
    private Member member;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @ColumnDefault("'1'")
    private Boolean pub;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GalleryFile> files = new ArrayList<>();

    public Gallery(String title, Member member) {
        this.title = title;
        this.member = member;
    }

    public void changeFiles(GalleryFile file) {
        this.files.add(file);
        file.setGallery(this);
    }
}
