package me.yh.community.service.impl;

import me.yh.community.Utils;
import me.yh.community.service.FileService;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

@Service
public class FileServiceImpl implements FileService {

    /**
     * 원본 이미지를 썸네일로 만들어 출력
     *
     * @param file   파일
     * @param width  너비
     * @param height 높이
     */
    public void toThumbnail(HttpServletResponse response, File file, int width, int height) throws IOException {
        //원본 이미지의 BufferedImage 객체를 생성
        BufferedImage oImg = ImageIO.read(file);
        ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
        //원본 이미지의 너비 높이 저장
        int oWidth = oImg.getWidth();
        int oHeight = oImg.getHeight();
        // 원본 너비를 기준으로 하여 만들고자하는 썸네일의 비율로 높이를 계산한다.
        int nWidth = oWidth;
        int nHeight = (oWidth / width) * height;

        // 계산된 높이가 원본보다 높다면 crop 이 안되므로
        // 원본 높이를 기준으로 썸네일의 비율로 너비를 계산한다.
        if (nHeight > oHeight) {
            nWidth = (oHeight / height) * width;
            nHeight = oHeight;
        }
        // 계산된 크기로 원본 이미지를 가운데에서 crop 한다.
        BufferedImage cropImg = Scalr.crop(oImg, (oWidth - nWidth) / 2, (oHeight - nHeight) / 2, nWidth, nHeight);
        // crop 된 이미지로 썸네일을 생성한다.
        BufferedImage thumbImg = Scalr.resize(cropImg, width, height);
        //만든 썸네일을 스트림에 저장
        ImageIO.write(thumbImg, "png", imageOutputStream);

        byte[] imgByte = imageOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-cache");   //인코더 해야 한글 이름으로도 다운로드 받을수 있다.
        response.addHeader("Content-disposition", "inline; fileName=" + Utils.urlEncode(file.getName()));
        response.setContentType("image/png");

        OutputStream out = response.getOutputStream();
        out.write(imgByte);
        out.close();
    }

    /**
     * @param folderPath 파일이 들어갈 폴더 경로
     * @param mf         단일 파일
     * @return 파일이 성공적으로 업로드 됐으면 새로운 파일이름, 실패 했으면 null
     */
    @Override
    public String upload(String folderPath, MultipartFile mf) {

        File folderFile = new File(folderPath);
        if (!folderFile.exists()) //폴더가 존재 하지 않으면 생성
        {
            boolean result = folderFile.mkdirs();
            if (!result) //폴더 생성 실패시
                return null;
        }
        String originalFileName = mf.getOriginalFilename();                  // 원본 파일 명
        String safeFileName = System.currentTimeMillis() + originalFileName; // 수정된 파일이름
        String safeFilePath = folderPath + SE + safeFileName;                // 파일경로

        try {
            mf.transferTo(new File(safeFilePath));
            return safeFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void download(HttpServletResponse response, File file, String originalFileName) throws IOException {

        if (originalFileName == null) {
            originalFileName = file.getName();
        }
        response.addHeader("Content-disposition", "attachment; fileName=" + Utils.urlEncode(originalFileName));

        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(file);
        byte[] buffer = new byte[1024 * 8];
        while (true) {
            int count = in.read(buffer); // 버퍼에 읽어들인 문자개수
            if (count == -1)             // 버퍼의 마지막에 도달했는지 체크
                break;
            out.write(buffer, 0, count);
        }
        in.close();
        out.close();
    }

    /**
     * @param filePath 삭제할 파일 경로
     * @return 삭제를 정상적으로 했으면  true 실패 했으면 false
     */
    @Override
    public boolean deleteFile(String filePath) {

        File file = new File(filePath);

        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
}
