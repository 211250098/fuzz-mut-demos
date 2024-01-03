package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperatorDao {

    @Autowired
    private List<Operator> operatorList;

    public boolean addOperator(Operator operator){
        operatorList.add(operator);
        operatorList.removeIf(o -> o.content == null);
        return true;
    }
}
