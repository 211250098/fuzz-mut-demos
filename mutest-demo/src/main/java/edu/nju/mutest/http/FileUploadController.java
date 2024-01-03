package edu.nju.mutest.http;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        String originalFilename = multipartFile.getOriginalFilename();//获取前端发过来的文件的原始文件名字

        String currentWorkingDirectory = System.getProperty("user.dir");
        // 构建相对路径
        String relativePath = "/mutest-demo/original_file";

        File file_dir = new File(currentWorkingDirectory, relativePath);//建立一个存储在本机的目录的file空对象
        if (file_dir.exists()) {
            FileUtils.forceDelete(file_dir);
            System.out.println("[LOG] Delete existing outDir.");
        }
        boolean mkdirs = file_dir.mkdirs();
        if (mkdirs) {
            System.out.println("[LOG] Get original_test successfully: " + file_dir.getAbsolutePath() + '/' + originalFilename);
        }

        File file = new File(file_dir, originalFilename);
        multipartFile.transferTo(file);

        return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
    }

}