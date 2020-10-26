package me.yh.community.repository;

import me.yh.community.entity.Post;
import me.yh.community.repository.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    boolean existsById(Long id);

    @Query("select max(p.groupOrder) from Post p where p.parent = :id")
    Integer findLastChildOrderById(@Param("id") Long id);

    /**
     *  서비스에서 이 이쿼리 다음부터 조회를 안하므로 클리어 필요 X
     *  해당 그룹번호에서 입력받은 그룹순서 이상의 그룹순서 번호를 1씩 증가함.
     */
    @Modifying
    @Query("update Post p set p.groupOrder = p.groupOrder +1 where p.groupNo = :groupNo and p.groupOrder >= :groupOrder")
    void incrementGroupOrder(@Param("groupNo") long groupNo, @Param("groupOrder") int groupOrder);
}
