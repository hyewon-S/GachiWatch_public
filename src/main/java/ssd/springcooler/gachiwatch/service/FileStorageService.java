package ssd.springcooler.gachiwatch.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

//    private final String uploadDir = "src/main/resources/static/image/profile"; // 저장 경로

    // application.properties에서 값 읽어오기
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String store(MultipartFile file) {
        System.out.println("store() 호출, 파일명: " + (file != null ? file.getOriginalFilename() : "null"));
        if (file == null || file.isEmpty()) {
            System.out.println("파일이 null이거나 비어있음");
            return null;
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + extension;

            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(dir, newFilename);
            file.transferTo(dest);

            System.out.println("현재 작업 디렉토리: " + System.getProperty("user.dir"));
            System.out.println("파일 저장 경로: " + dest.getAbsolutePath());

            return "/image/profile/" + newFilename; // DB에 저장할 상대 경로
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}

