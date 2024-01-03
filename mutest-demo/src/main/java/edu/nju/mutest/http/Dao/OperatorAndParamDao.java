package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.OperatorAndParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperatorAndParamDao {

    @Autowired
    private List<OperatorAndParam> operatorAndParamList;

    public boolean add(String operator,String param){
        operatorAndParamList.add(new OperatorAndParam(operator,param));
        operatorAndParamList.removeIf(o -> o.param == null);
        System.out.println(operatorAndParamList);
        return true;
    }
}
