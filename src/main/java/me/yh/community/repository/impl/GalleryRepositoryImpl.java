package me.yh.community.repository.impl;

import me.yh.community.Utils;
import me.yh.community.dto.gallery.GalleryListDto;
import me.yh.community.repository.custom.GalleryRepositoryCustom;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GalleryRepositoryImpl implements GalleryRepositoryCustom {

    private final DataSource dataSource;

    public GalleryRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<GalleryListDto> findListByPageAndPub(long page, Boolean pub) {

        List<GalleryListDto> list = new ArrayList<>();
        long offset = (page-1) *10;
        String sql =
                "SELECT F.gallery_id id , F.FILE_NAME fileName, CNT childCount " +
                "FROM GALLERY_FILE F " +
                "JOIN( " +
                "    SELECT MIN(gallery_file_id) id, COUNT(gallery_id) CNT " +
                "    FROM GALLERY_FILE " +
                "    GROUP BY gallery_id " +
                ")B ON F.gallery_file_id = B.id " +
                "JOIN GALLERY G ON F.gallery_id = G.gallery_id " +
                "WHERE pub = ? " +
                "ORDER BY F.gallery_id DESC " +
                "OFFSET ? LIMIT 40";

        try(Connection connection = dataSource.getConnection()) {

            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setBoolean(1, pub);
            pst.setLong(2, offset);

            final ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                final Long id = rs.getLong("id");
                final String fileName = Utils.urlEncode(rs.getString("fileName"));
                final int childCount = rs.getInt("childCount");
                GalleryListDto g = new GalleryListDto(id, fileName, childCount, null);
                list.add(g);
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<GalleryListDto> findListByPage(long page) {

        List<GalleryListDto> list = new ArrayList<>();
        long offset = (page-1) *10;
        String sql =
                "SELECT F.gallery_id id , F.FILE_NAME fileName, CNT childCount, G.pub " +
                "FROM GALLERY_FILE F " +
                "JOIN( " +
                "    SELECT MIN(gallery_file_id) id, COUNT(gallery_id) CNT " +
                "    FROM GALLERY_FILE " +
                "    GROUP BY gallery_id " +
                ")B ON F.gallery_file_id = B.id " +
                "JOIN GALLERY G ON F.gallery_id = G.gallery_id " +
                "ORDER BY F.gallery_id DESC " +
                "OFFSET ? LIMIT 40";

        try(Connection connection = dataSource.getConnection()) {

            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setLong(1, offset);

            final ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                final Long id = rs.getLong("id");
                final String fileName = Utils.urlEncode(rs.getString("fileName"));
                final int childCount = rs.getInt("childCount");
                final Boolean pub = rs.getBoolean("pub");
                GalleryListDto g = new GalleryListDto(id, fileName, childCount, pub);
                list.add(g);
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
