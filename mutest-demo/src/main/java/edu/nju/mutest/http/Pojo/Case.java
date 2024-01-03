package edu.nju.mutest.http.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case {

    public String id;
    public byte[] file;
}
