package edu.nju.mutest.http;

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
    static File file ;

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        try {
            file = convertMultipartFileToFile(multipartFile);
            // 在这里可以对文件进行进一步处理，例如保存到磁盘或解析文件内容
            // ...


            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            // 处理文件上传异常
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public  ResponseEntity<String> updateResource(@PathVariable("id") String resourceId, @RequestParam("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        try {
            // 将 MultipartFile 转换为 File
            file = convertMultipartFileToFile(multipartFile);

            // 在这里执行对上传的文件进行处理的逻辑
            // 可以根据 resourceId 和 uploadedFile 进行相应的处理

            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }
        return file;
    }

    public static File getFile(){
        return file;
    }
}