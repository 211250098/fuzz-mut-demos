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

import java.util.List;

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
    public String addCase(@RequestBody byte[] file){
        caseService.addCase(file);
        return "ok";
    }

    @PutMapping("/case/{id}")
    public String updateCase(@PathVariable("id") Long id,@RequestBody byte[] file){
        caseService.updateCase(id,file);
        return "ok";
    }

    @DeleteMapping("/case/{id}")
    public String removeCase(@PathVariable("id") Long id){
        caseService.removeCase(id);
        return "ok";
    }

    @PostMapping("/operator")
    public String operator(@RequestBody Operator o){
        operatorService.addOperator(o);
        return "ok";
    }

    @PostMapping("/param")
    public String param(@RequestBody Param param){
        paramService.addParam(param);
        return "ok";
    }
}
