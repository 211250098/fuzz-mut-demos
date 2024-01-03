package edu.nju.mutest.http.Controller;

import edu.nju.mutest.DemoMutantExecution;
import edu.nju.mutest.DemoSrcMutationEngine;
import edu.nju.mutest.http.Dao.DataDao;
import edu.nju.mutest.http.MyFile;
import edu.nju.mutest.http.Pojo.*;
import edu.nju.mutest.http.Service.*;
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
    public List<Case> getCase(){
        return caseService.getCaseList();
    }

    @GetMapping("/case/{id}")
    public Case getCaseById(@PathVariable("id") String id){
        return caseService.getCaseById(id);
    }

    @PostMapping("/case")
    public String addCase(@RequestParam("file") MultipartFile file) throws IOException {
        caseService.addCase(file.getBytes());
        return "ok";
    }

    @PutMapping("/case/{id}")
    public String updateCase(@PathVariable("id") String id,@RequestParam("file") MultipartFile file) throws IOException {
        caseService.updateCase(id,file.getBytes());
        return "ok";
    }

    @DeleteMapping("/case/{id}")
    public String removeCase(@PathVariable("id") String id){
        caseService.removeCase(id);
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
