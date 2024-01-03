package edu.nju.mutest.http.Dao;

import edu.nju.mutest.http.Pojo.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperatorDao {

    @Autowired
    private List<Operator> operator;

    public boolean addOperator(Operator o){
        operator.add(o);
        return true;
    }
}
