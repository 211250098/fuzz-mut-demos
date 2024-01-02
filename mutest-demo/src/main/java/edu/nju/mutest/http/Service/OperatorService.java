package edu.nju.mutest.http.Service;

import edu.nju.mutest.http.Dao.OperatorDao;
import edu.nju.mutest.http.Pojo.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperatorService {

    @Autowired
    private OperatorDao operatorDao;

    public boolean addOperator(Operator o){
        operatorDao.addOperator(o);
        return true;
    }
}
