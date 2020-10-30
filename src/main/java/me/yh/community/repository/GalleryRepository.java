package me.yh.community.repository;

import me.yh.community.entity.Gallery;
import me.yh.community.repository.custom.GalleryRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long>, GalleryRepositoryCustom {

    @EntityGraph(attributePaths = "files")
    Optional<Gallery> findById(Long id);

    long countListByPub(boolean b);

    boolean existsGalleryDetailByIdAndPub(long galleryId, boolean b);

    @Query("select g from Gallery g join fetch g.files where g.id = :id and g.pub = :pub")
    Optional<Gallery> findGalleryDetailByIdAndPub(@Param("id") long galleryId,@Param("pub") boolean pub);

    @Transactional
    @Modifying
    @Query("delete from Gallery g where g.id = :id")
    void deleteById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("delete from GalleryFile gf where gf.id in :ids")
    void deleteAllFileByIds(@Param("ids") List<Long> ids);

    @Transactional
    @Modifying
    @Query("update Gallery g set g.pub= :pub where g.id= :id")
    void changePubById(@Param("id") long id, @Param("pub") boolean pub);
}

