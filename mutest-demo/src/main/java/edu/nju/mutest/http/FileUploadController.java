package edu.nju.mutest.http;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        String originalFilename = multipartFile.getOriginalFilename();//获取前端发过来的文件的原始文件名字
        File file_dir = new File("C:\\Users\\admin\\Desktop\\fuzz-mut-demos\\mutest-demo\\original_file");//建立一个存储在本机的目录的file空对象
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