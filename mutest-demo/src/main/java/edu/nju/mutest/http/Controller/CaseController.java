package edu.nju.mutest.http.Controller;

import edu.nju.mutest.DemoMutantExecution;
import edu.nju.mutest.DemoSrcMutationEngine;
import edu.nju.mutest.Utils;
import edu.nju.mutest.http.Dao.DataDao;
import edu.nju.mutest.http.MyFile;
import edu.nju.mutest.http.Pojo.Case;
import edu.nju.mutest.http.Service.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CaseService caseService;
    @Autowired
    private ParamService paramService;
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private ResultService resultService;
    @Autowired
    private OperatorAndParamService operatorAndParamService;

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

    @GetMapping("/case/{id}")
    public Case getCaseById(@PathVariable("id") String id){
        return caseService.getCaseById(id);
    }

    @PostMapping("/case")
    public String addCase(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return "File is empty";
        }

        String originalFilename = multipartFile.getOriginalFilename();//获取前端发过来的文件的原始文件名字
        String pattern = "test-%d/%s";
        File file_dir = new File("C:\\Users\\admin\\Desktop\\fuzz-mut-demos\\mutest-demo\\testsuite_files");//建立一个存储在本机的目录的file空对象
        File[] listfile = file_dir.listFiles();


        File file = new File(file_dir, String.format(pattern, listfile.length + 1, originalFilename));
        boolean mkdirs = file.mkdirs();
        if (mkdirs) {
            System.out.println("[LOG] Get test files successfully: " + file.getAbsolutePath());
        }
        multipartFile.transferTo(file);

//        caseService.addCase(multipartFile.getBytes());
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
        File file_dir = new File("C:\\Users\\admin\\Desktop\\fuzz-mut-demos\\mutest-demo\\testsuite_files");
        List<File> list_files = Utils.getAllFiles(file_dir,".java");
        FileUtils.delete(list_files.get(Integer.parseInt(id)));
        System.out.println(id);

        return "ok";
    }

    @PostMapping("/operatorAndParam")
    public String operatorAndParam(@RequestBody Map map) throws IOException, InterruptedException {
        operatorAndParamService.add((String) map.get("operator"), (String) map.get("parameters"));
        DemoSrcMutationEngine.MutationEngine();
        String[][] res = DemoMutantExecution.MutantExecute();
        dataDao.setRes(res);
        return "ok";
    }

//    @PostMapping("/operator")
//    public String operator(@RequestParam("operator") Operator o){
//        operatorService.addOperator(o);
//        return "ok";
//    }
//
//    @PostMapping("/param")
//    public String param(@RequestParam("param") Param param){
//        paramService.addParam(param);
//        return "ok";
//    }

}
