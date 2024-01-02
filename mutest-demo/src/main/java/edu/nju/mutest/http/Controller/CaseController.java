package edu.nju.mutest.http.Controller;

import edu.nju.mutest.http.Pojo.Case;
import edu.nju.mutest.http.Pojo.Operator;
import edu.nju.mutest.http.Pojo.Param;
import edu.nju.mutest.http.Service.CaseService;
import edu.nju.mutest.http.Service.OperatorService;
import edu.nju.mutest.http.Service.ParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CaseController {
    @Autowired
    private CaseService caseService;
    @Autowired
    private ParamService paramService;
    @Autowired
    private OperatorService operatorService;

    @GetMapping("/result")
    public String result(){
        return "success";
    }

    @PostMapping("/case")
    public String addCase(@RequestBody Case c){
        caseService.addCase(c);
        return "ok";
    }

    @PutMapping("/case/{id}")
    public String updateCase(@PathVariable("id") Long id,@RequestBody Case c){
        caseService.updateCase(id,c);
        return "ok";
    }

    @DeleteMapping("/case")
    public String removeCase(@RequestBody Case c){
        caseService.removeCase(c);
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
