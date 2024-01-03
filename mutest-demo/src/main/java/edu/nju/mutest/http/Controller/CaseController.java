package edu.nju.mutest.http.Controller;

import edu.nju.mutest.DemoMutantExecution;
import edu.nju.mutest.DemoSrcMutationEngine;
import edu.nju.mutest.Utils;
import edu.nju.mutest.http.Dao.DataDao;
import edu.nju.mutest.http.MyFile;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CaseController {
    DataDao dataDao = new DataDao();

    @GetMapping("/result")
    public String[][] result() throws IOException, InterruptedException {
        return dataDao.getRes();
    }

    @GetMapping("/mutants")
    public MyFile[] mutants() throws IOException {
        return DemoMutantExecution.getMutFiles();
    }

    @GetMapping("/case")
    public MyFile[] getCase() throws IOException {
        return Utils.getMutFiles();
    }


    @PostMapping("/case")
    public String addCase(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return "File is empty";
        }

        String originalFilename = multipartFile.getOriginalFilename();//获取前端发过来的文件的原始文件名字
        String pattern = "test-%d/%s";

        String currentWorkingDirectory = System.getProperty("user.dir");
        // 构建相对路径
        String relativePath = "/mutest-demo/testsuite_files";

        File file_dir = new File(currentWorkingDirectory, relativePath);//建立一个存储在本机的目录的file空对象
        File[] listfile = file_dir.listFiles();
        File file;
        if (listfile == null) {
            file = new File(file_dir, String.format(pattern, 1, originalFilename));
        }
        else file = new File(file_dir, String.format(pattern, listfile.length + 1, originalFilename));
        boolean mkdirs = file.mkdirs();
        if (mkdirs) {
            System.out.println("[LOG] Get test files successfully: " + file.getAbsolutePath());
        }
        multipartFile.transferTo(file);
        return "ok";
    }

    @PutMapping("/case/{id}")
    public String updateCase(@PathVariable("id") String id,@RequestParam("file") MultipartFile file) throws IOException {

        removeCase(id);

        addCase(file);


        return "ok";
    }

    @DeleteMapping("/case/{id}")
    public String removeCase(@PathVariable("id") String id) throws IOException {

        System.out.println(id);

        String currentWorkingDirectory = System.getProperty("user.dir");

        // 构建相对路径
        String relativePath = "/mutest-demo/testsuite_files";

        File file_dir = new File(currentWorkingDirectory, relativePath);
        List<File> list_files = Utils.getAllFiles(file_dir,".java");
        FileUtils.delete(list_files.get(Integer.parseInt(id)));
        System.out.println(id);

        return "ok";
    }

    @PostMapping("/operatorAndParam")
    public String operatorAndParam(@RequestBody Map map) throws IOException, InterruptedException {
        dataDao.setOperator((String) map.get("operator"));
        DemoSrcMutationEngine.MutationEngine();
        String[][] res = DemoMutantExecution.MutantExecute();
        dataDao.setRes(res);
        return "ok";
    }
}
