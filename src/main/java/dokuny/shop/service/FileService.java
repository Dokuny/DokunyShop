package dokuny.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    public String uploadFile(String uploadPath,String originalFileName,byte[] fileData) throws Exception {

        UUID uuid = UUID.randomUUID();

        // 확장자 분리
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // uuid 로 새로운 파일명을 만들고 거기에 확장자 붙여주기.
        String savedFileName = uuid.toString() + extension;

        // 저장 경로 + / + 새로운 파일명
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);

        fos.write(fileData);

        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("delete file : {}",filePath);
        }else {
            log.info("file doesn't exist : {}",filePath);
        }
    }
}
