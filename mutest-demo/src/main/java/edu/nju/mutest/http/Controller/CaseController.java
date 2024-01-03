package edu.nju.mutest.http.Controller;

import edu.nju.mutest.http.Pojo.Case;
import edu.nju.mutest.http.Pojo.Operator;
import edu.nju.mutest.http.Pojo.Param;
import edu.nju.mutest.http.Pojo.Result;
import edu.nju.mutest.http.Service.CaseService;
import edu.nju.mutest.http.Service.OperatorService;
import edu.nju.mutest.http.Service.ParamService;
import edu.nju.mutest.http.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:9526")
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

    @GetMapping("/result/{id}")
    public Result result(@PathVariable("id") Long id){
        return resultService.getResult(id);
    }

    @GetMapping("/case")
    public List<Case> getCase(){
        return caseService.getCaseList();
    }

    @GetMapping("/case/{id}")
    public Case getCaseById(@PathVariable("id") Long id){
        return caseService.getCaseById(id);
    }

    @PostMapping("/case")
    public String addCase(@RequestParam("file") MultipartFile file) throws IOException {
        caseService.addCase(file.getBytes());
        return "ok";
    }

    @PutMapping("/case/{id}")
    public String updateCase(@PathVariable("id") Long id,@RequestParam("file") MultipartFile file) throws IOException {
        caseService.updateCase(id,file.getBytes());
        return "ok";
    }

    @DeleteMapping("/case/{id}")
    public String removeCase(@PathVariable("id") Long id){
        caseService.removeCase(id);
        return "ok";
    }

    @PostMapping("/operator")
    public String operator(@RequestParam("operator") Operator o){
        operatorService.addOperator(o);
        return "ok";
    }

    @PostMapping("/param")
    public String param(@RequestParam("param") Param param){
        paramService.addParam(param);
        return "ok";
    }
}
