package edu.nju.mutest.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MyFile {
    String FileName;

    @JsonProperty("Content")
    String[] Content;

    public MyFile() {

    }

    public MyFile(String fileName, String[] content) {
        FileName = fileName;
        Content = content;
    }
}
