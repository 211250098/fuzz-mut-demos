package edu.nju.mutest;

import edu.nju.mutest.http.MyFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static MyFile[] getMutFiles() throws IOException {
        File test_files = new File("C:\\Users\\admin\\Desktop\\fuzz-mut-demos\\mutest-demo\\testsuite_files");
        List<File> files = getAllFiles(test_files, ".java");
        MyFile[] mutFiles = new MyFile[files.size()];
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            br.close();
            MyFile myFile = new MyFile("test-" + (i + 1), content.toString().split("\n"));
            mutFiles[i] = myFile;
        }
        return mutFiles;
    }

    public static List<File> getAllFiles(File tsDir, String suffix) {
        List<File> AllFiles = new ArrayList<>();
        if (!tsDir.isFile()) {//文件夹
            File[] files = tsDir.listFiles();
            if (tsDir.exists() && files != null) {
                for (File file : files) {
                    AllFiles.addAll(getAllFiles(file, suffix));
                }
            }
        }
        else {//文件
            if (tsDir.getAbsolutePath().endsWith(suffix)) {
                AllFiles = List.of(tsDir);
            }
        }
        return AllFiles;
    }

}